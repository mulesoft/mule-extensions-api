/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax;

import static java.util.Optional.empty;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.text.WordUtils.capitalize;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.allowsInlineDefinition;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getAlias;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isInfrastructure;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.sanitizeName;
import static org.mule.runtime.extension.api.util.XmlModelUtils.supportsTopLevelDeclaration;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Utils class with helper methods for the {@link DslSyntaxResolver}
 *
 * @since 1.0
 */
public final class DslSyntaxUtils {

  private final static String CONNECTION_PROVIDER_SUFFIX = "connection";
  private final static String CONFIGURATION_SUFFIX = "config";
  private final static Pattern SANITIZE_PATTERN = compile("\\s+");

  private DslSyntaxUtils() {}

  /**
   * Provides a sanitized, hyphenized, space-free name that can be used as an XML element-name
   * for a given {@link NamedObject}
   *
   * @param component the {@link NamedObject} who's name we want to convert
   * @return a sanitized, hyphenized, space-free name that can be used as an XML element-name
   */
  static String getSanitizedElementName(NamedObject component) {
    String name = SANITIZE_PATTERN.matcher(hyphenize(sanitizeName(capitalize(component.getName())))).replaceAll("");
    if (component instanceof ConfigurationModel) {
      return appendSuffix(name, CONFIGURATION_SUFFIX);
    }
    if (component instanceof ConnectionProviderModel) {
      return appendSuffix(name, CONNECTION_PROVIDER_SUFFIX);
    }
    return name;
  }

  private static String appendSuffix(String name, String suffix) {
    return !name.toLowerCase().endsWith(suffix) ? name.concat("-" + suffix) : name;
  }

  static boolean isValidBean(ObjectType objectType) {
    if (objectType.getAnnotation(ClassInformationAnnotation.class).isPresent()) {
      return isInstantiable(objectType) && !objectType.getFields().isEmpty();
    }

    return objectType.getAnnotation(TypeDslAnnotation.class)
        .map(dsl -> dsl.allowsInlineDefinition() || dsl.allowsTopLevelDefinition())
        .orElse(false);
  }

  static boolean isFlattened(ObjectFieldType field, MetadataType fieldValue) {
    return fieldValue instanceof ObjectType && field.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  static Optional<String> getTypeKey(MetadataType type, String namespace, String namespaceUri) {
    Optional<String> id = getId(type);
    if (id.isPresent()) {
      return of(id.get() + namespace + namespaceUri);
    }
    return !isEmpty(getAlias(type)) ? of(getAlias(type) + namespace + namespaceUri) : empty();
  }

  static boolean isText(ParameterModel parameter) {
    return parameter.getLayoutModel().map(LayoutModel::isText).orElse(false);
  }

  static boolean isText(MetadataType type) {
    return type.getAnnotation(LayoutTypeAnnotation.class).map(layoutTypeAnnotation -> layoutTypeAnnotation.isText())
        .orElse(false);
  }

  static boolean isInstantiable(MetadataType metadataType) {
    Optional<ClassInformationAnnotation> classInformation = metadataType.getAnnotation(ClassInformationAnnotation.class);
    return classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
  }

  /**
   * Check's if a type is an {@link ExtensibleTypeAnnotation extensible type}
   *
   * @param metadataType the {@link MetadataType} to verify for it's extensibility
   * @return {@code true} if the given type is annotated with {@link ExtensibleTypeAnnotation}
   */
  static boolean isExtensible(MetadataType metadataType) {
    return metadataType.getAnnotation(ExtensibleTypeAnnotation.class).isPresent();
  }

  static boolean supportAttributeDeclaration(MetadataType metadataType) {
    return metadataType.getAnnotation(ParameterDslAnnotation.class)
        .map(fieldDsl -> metadataType.getAnnotation(ExpressionSupportAnnotation.class)
            .map(ExpressionSupportAnnotation::getExpressionSupport)
            .map(exprSupport -> !(exprSupport.equals(NOT_SUPPORTED) && !fieldDsl.allowsReferences())).orElse(true))
        .orElse(true);
  }

  static boolean supportTopLevelElement(MetadataType metadataType) {
    return supportTopLevelElement(metadataType, metadataType.getAnnotation(ParameterDslAnnotation.class)
        .map(ParameterDslAnnotation::allowsReferences).orElse(true));
  }

  static boolean supportTopLevelElement(MetadataType metadataType, ParameterDslConfiguration dslConfiguration) {
    return supportTopLevelElement(metadataType, dslConfiguration.allowsReferences());
  }

  static boolean supportTopLevelElement(MetadataType metadataType, boolean allowsReferences) {
    if (!allowsReferences) {
      return false;
    }

    final AtomicBoolean supporstGlobalDeclaration = new AtomicBoolean(false);
    metadataType.accept(new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
        supporstGlobalDeclaration.set(supportsTopLevelDeclaration(objectType) && isValidBean(objectType));
      }
    });

    return supporstGlobalDeclaration.get();
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport) {
    return supportsInlineDeclaration(metadataType, expressionSupport, ParameterDslConfiguration.getDefaultInstance(), false);
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport, boolean isContent) {
    return supportsInlineDeclaration(metadataType, expressionSupport, ParameterDslConfiguration.getDefaultInstance(), isContent);
  }

  public static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport,
                                                  ParameterDslConfiguration dslModel, boolean isContent) {
    final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

    if (isContent) {
      return true;
    } else if (REQUIRED == expressionSupport) {
      return false;
    } else if (!dslModel.allowsInlineDefinition()) {
      return false;
    }

    metadataType.accept(new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        supportsChildDeclaration.set(true);
      }

      @Override
      public void visitAnyType(AnyType anyType) {
        supportsChildDeclaration.set(false);
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        final MetadataTypeVisitor currentVisitor = this;
        arrayType.getType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitObject(ObjectType objectType) {
            if (isMap(objectType)) {
              supportsChildDeclaration.set(false);
            } else {
              objectType.accept(currentVisitor);
            }
          }

          @Override
          protected void defaultVisit(MetadataType metadataType) {
            metadataType.accept(currentVisitor);
          }
        });
      }

      @Override
      public void visitObject(ObjectType objectType) {
        if (isMap(objectType)) {
          supportsChildDeclaration.set(true);
        } else {
          supportsChildDeclaration.set((isValidBean(objectType) || isInfrastructure(objectType))
              && allowsInlineDefinition(objectType));
        }
      }

      @Override
      public void visitUnion(UnionType unionType) {
        supportsChildDeclaration.set(false);
      }
    });

    return supportsChildDeclaration.get();
  }

  public static boolean typeRequiresWrapperElement(MetadataType metadataType, TypeCatalog typeCatalog) {
    return metadataType instanceof ObjectType &&
        (isExtensible(metadataType) || typeCatalog.containsBaseType((ObjectType) metadataType));
  }

}
