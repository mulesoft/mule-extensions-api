/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.supportsInlineDeclaration;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.typeRequiresWrapperElement;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.WIRING;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isReferableType;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;

import static com.google.common.base.Functions.identity;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.model.impl.DefaultObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enhances the declaration of the {@link ParameterDslConfiguration} taking into account the type of the parameter as well as the
 * context in which the type is being used.
 *
 * @since 4.1.3, 4.2.0
 */
public class ParameterDslDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return WIRING;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalker(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
      TypeCatalog typeCatalog = extensionLoadingContext.getDslResolvingContext().getTypeCatalog();

      @Override
      protected void onParameter(ParameterGroupDeclaration parameterGroup, ParameterDeclaration declaration) {

        ParameterDslConfiguration.Builder builder = ParameterDslConfiguration.builder();
        boolean isContent = !declaration.getRole().equals(ParameterRole.BEHAVIOUR);
        ParameterDslConfiguration dslConfiguration = declaration.getDslConfiguration();

        resolveType(typeCatalog, declaration)
            .accept(new MetadataTypeVisitor() {

              @Override
              protected void defaultVisit(MetadataType metadataType) {
                builder.allowsInlineDefinition(dslConfiguration.allowsInlineDefinition() && isContent)
                    .allowTopLevelDefinition(false)
                    .allowsReferences(false);
              }

              @Override
              public void visitString(StringType stringType) {
                boolean isText = declaration.getLayoutModel() != null && declaration.getLayoutModel().isText();
                builder.allowsInlineDefinition(dslConfiguration.allowsInlineDefinition() && (isText || isContent))
                    .allowTopLevelDefinition(false)
                    .allowsReferences(false);
              }

              @Override
              public void visitArrayType(ArrayType arrayType) {
                MetadataType genericType = arrayType.getType();
                boolean supportsInline = supportsInlineDeclaration(arrayType, declaration.getExpressionSupport(),
                                                                   dslConfiguration, isContent);
                boolean isWrapped = allowsInlineAsWrappedType(genericType, typeCatalog);

                builder.allowsInlineDefinition(dslConfiguration.allowsInlineDefinition() && (supportsInline || isWrapped))
                    .allowTopLevelDefinition(dslConfiguration.allowTopLevelDefinition())
                    .allowsReferences(dslConfiguration.allowsReferences());
              }

              @Override
              public void visitAnyType(AnyType anyType) {
                if (isReferableType(anyType)) {
                  builder.allowsReferences(dslConfiguration.allowsReferences())
                      .allowTopLevelDefinition(false)
                      .allowsInlineDefinition(false);
                } else {
                  defaultVisit(anyType);
                }
              }

              @Override
              public void visitObject(ObjectType objectType) {
                if (isMap(objectType)) {
                  builder.allowsInlineDefinition(dslConfiguration.allowsInlineDefinition());

                } else if (!declaration.getModelProperty(InfrastructureParameterModelProperty.class).isPresent()) {

                  boolean supportsInline = supportsInlineDeclaration(objectType, declaration.getExpressionSupport(),
                                                                     dslConfiguration, isContent);

                  boolean isWrapped = allowsInlineAsWrappedType(objectType, typeCatalog);

                  builder.allowsInlineDefinition(dslConfiguration.allowsInlineDefinition() &&
                      (supportsInline || isWrapped));
                }

                builder.allowTopLevelDefinition(dslConfiguration.allowTopLevelDefinition())
                    .allowsReferences(dslConfiguration.allowsReferences());
              }

              @Override
              public void visitUnion(UnionType unionType) {
                unionType.getTypes().forEach(type -> type.accept(this));
              }

            });

        declaration.setDslConfiguration(builder.build());
      }

      /**
       * Resolves the MetadataType that represents the type of the parameter {@code declaration}, removing any information from it
       * that is specific to its role as parameter.
       * <p>
       * For instance, A {@link MetadataType} representing an object defined in a JSON schema may be declared as an
       * {@link InputStream} in one parameter or as a {@link Map} in another one, but the type both reference is the same. This
       * method causes both uses to be considered the same type.
       */
      private MetadataType resolveType(TypeCatalog typeCatalog, ParameterDeclaration declaration) {
        return getTypeId(declaration.getType())
            // Get the type instance from the extension types to keep the flyweight working correctly
            .map(typeId -> typeCatalog.getType(typeId).orElse(extensionDeclaration.getTypeById(typeId)))
            .map((MetadataType type) -> {
              if (type instanceof ObjectType) {
                final Map<Class<? extends TypeAnnotation>, TypeAnnotation> normalizedAnnotationsByClass =
                    type.getAnnotations().stream()
                        .collect(toMap(TypeAnnotation::getClass, identity(), (u, v) -> v, LinkedHashMap::new));

                // the type to be processed has to have the annotations that were specifically set for this type as a parameter
                declaration.getType().getAnnotation(ClassInformationAnnotation.class)
                    .ifPresent(paramClassInfo -> normalizedAnnotationsByClass.put(ClassInformationAnnotation.class,
                                                                                  paramClassInfo));
                declaration.getType().getAnnotation(ParameterDslAnnotation.class)
                    .ifPresent(paramClassInfo -> normalizedAnnotationsByClass.put(ParameterDslAnnotation.class,
                                                                                  paramClassInfo));
                return new DefaultObjectType(((ObjectType) type).getFields(), ((ObjectType) type).isOrdered(),
                                             ((ObjectType) type).getOpenRestriction().orElse(null),
                                             type.getMetadataFormat(), normalizedAnnotationsByClass);
              } else {
                return type;
              }
            })
            .orElse(declaration.getType());
      }

      boolean allowsInlineAsWrappedType(MetadataType type, TypeCatalog typeCatalog) {
        return isSubTypeBase(type)
            || typeRequiresWrapperElement(type, typeCatalog);
      }

      /**
       * Checks if the given {@code type} is a base type for subtypes of the extension.
       * <p>
       * This method checks by type id rather than equality, since different instances of MetadataType may refer to the same type,
       * some already enriched and others not.
       */
      private boolean isSubTypeBase(MetadataType type) {
        final Optional<String> typeIdOptional = getTypeId(type);

        return extensionDeclaration.getSubTypes()
            .stream()
            .map(subType -> subType.getBaseType())
            .anyMatch(baseType -> typeIdOptional
                .map(typeId -> getTypeId(baseType)
                    .map(baseTypeId -> typeId.equals(baseTypeId))
                    .orElse(false))
                .orElseGet(() -> baseType.equals(type)));
      }
    });
  }
}
