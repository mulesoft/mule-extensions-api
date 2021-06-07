/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.sort;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.util.FunctionalUtils.computeIfAbsent;
import static org.mule.runtime.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.getSanitizedElementName;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.getTypeId;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.getTypeKey;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.isExtensible;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.isFlattened;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.isInstantiable;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.isText;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.isValidBean;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.supportAttributeDeclaration;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.supportTopLevelElement;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.supportsInlineDeclaration;
import static org.mule.runtime.extension.api.dsl.syntax.DslSyntaxUtils.typeRequiresWrapperElement;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isInfrastructure;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.requiresConfig;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.internal.dsl.DslConstants.KEY_ATTRIBUTE_NAME;
import static org.mule.runtime.internal.dsl.DslConstants.VALUE_ATTRIBUTE_NAME;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ComponentModelVisitor;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.connection.HasConnectionProviderModels;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DefaultImportTypesStrategy;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.dsl.syntax.resolver.ImportTypesStrategy;
import org.mule.runtime.extension.api.property.QNameModelProperty;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.namespace.QName;

import com.google.common.collect.ImmutableSet;

/**
 * Default implementation of a {@link DslSyntaxResolver} based on XML.
 * <p>
 * Provides the {@link DslElementSyntax} of any {@link NamedObject Component}, {@link ParameterModel Parameter} or
 * {@link MetadataType Type} within the context of the {@link ExtensionModel Extension model} where the Component was declared.
 *
 * @since 1.0
 */
public class XmlDslSyntaxResolver implements DslSyntaxResolver {

  private final ExtensionModel extensionModel;
  private final TypeCatalog typeCatalog;
  private final XmlDslModel languageModel;
  private final Map<String, DslElementSyntax> resolvedTypes = new HashMap<>();
  private final Map<MetadataType, XmlDslModel> importedTypes;
  private final Deque<String> typeResolvingStack = new ArrayDeque<>();
  private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  /**
   * Creates an instance using the default implementation
   *
   * @param model   the {@link ExtensionModel} that provides context for resolving the component's {@link DslElementSyntax}
   * @param context the {@link DslResolvingContext} in which the Dsl resolution takes place
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *                                  present in the provided {@link DslResolvingContext} or if the imported
   *                                  {@link ExtensionModel} doesn't have any {@link ImportedTypeModel}
   */
  public XmlDslSyntaxResolver(ExtensionModel model, DslResolvingContext context) {
    this.extensionModel = model;
    this.languageModel = model.getXmlDslModel();
    this.typeCatalog = getTypeCatalog(model, context);
    this.importedTypes = new DefaultImportTypesStrategy(model, context).getImportedTypes();
  }

  /**
   * Creates an instance using the default implementation
   *
   * @param model               the {@link ExtensionModel} that provides context for resolving the component's
   *                            {@link DslElementSyntax}
   * @param importTypesStrategy the {@link ImportTypesStrategy} used for external types resolution
   * @return the default implementation of a {@link DslSyntaxResolver}
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *                                  present in the provided {@link DslResolvingContext} or if the imported
   *                                  {@link ExtensionModel} doesn't have any {@link ImportedTypeModel}
   */
  public XmlDslSyntaxResolver(ExtensionModel model, ImportTypesStrategy importTypesStrategy) {
    this.extensionModel = model;
    this.languageModel = model.getXmlDslModel();
    this.typeCatalog = TypeCatalog.getDefault(singleton(model));
    this.importedTypes = importTypesStrategy.getImportedTypes();
  }

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link NamedObject component}.
   *
   * @param component the {@link NamedObject} element to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link NamedObject model}
   */
  @Override
  public DslElementSyntax resolve(final NamedObject component) {
    final String elementName = getSanitizedElementName(component);
    return computeIfAbsent(resolvedTypes, elementName, key -> {
      DslElementSyntaxBuilder dsl = DslElementSyntaxBuilder.create()
          .withElementName(elementName)
          .withNamespace(languageModel.getPrefix(), languageModel.getNamespace())
          .supportsTopLevelDeclaration(component instanceof ConstructModel
              && ((ConstructModel) component).allowsTopLevelDeclaration())
          .supportsChildDeclaration(true)
          .supportsAttributeDeclaration(false)
          .requiresConfig(requiresConfig(extensionModel, component));

      if (component instanceof ComponentModel) {
        resolveComponentDsl((ComponentModel) component, dsl);
      } else {
        if (component instanceof ParameterizedModel) {
          resolveParameterizedDsl((ParameterizedModel) component, dsl);
        }
        if (component instanceof HasConnectionProviderModels) {
          ((HasConnectionProviderModels) component).getConnectionProviders()
              .forEach(c -> dsl.containing(c.getName(), resolve(c)));
        }
      }
      return dsl.build();
    });
  }

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link ParameterModel parameter}, providing all the required information
   * for representing this {@code parameter} element in the DSL.
   *
   * @param parameter the {@link ParameterModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterModel parameter}
   */
  @Override
  public DslElementSyntax resolve(final ParameterModel parameter) {
    final ExpressionSupport expressionSupport = parameter.getExpressionSupport();
    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
    final ParameterDslConfiguration dslConfig = parameter.getDslConfiguration();
    final boolean isContent = isContent(parameter);

    Reference<String> prefix = new Reference<>(languageModel.getPrefix());
    Reference<String> namespace = new Reference<>(languageModel.getNamespace());
    Reference<String> elementName = new Reference<>(hyphenize(parameter.getName()));

    getCustomQName(parameter).ifPresent(qName -> {
      elementName.set(qName.getLocalPart());
      prefix.set(qName.getPrefix());
      namespace.set(qName.getNamespaceURI());
    });

    parameter.getType().accept(
                               new MetadataTypeVisitor() {

                                 @Override
                                 public void visitUnion(UnionType unionType) {
                                   final boolean extensible = isExtensible(parameter.getType());
                                   builder.asWrappedElement(extensible);

                                   if (extensible) {
                                     addAttributeName(builder, parameter, isContent, dslConfig);
                                     builder.withNamespace(prefix.get(), namespace.get())
                                         .withElementName(elementName.get());

                                     unionType.getTypes().forEach(type -> getTypeId(type)
                                         .ifPresent(typeId -> resolve(type)
                                             .ifPresent(typeDsl -> builder.containing(typeId, typeDsl))));
                                   } else {
                                     unionType.getTypes().forEach(type -> type.accept(this));
                                   }
                                 }

                                 @Override
                                 protected void defaultVisit(MetadataType metadataType) {
                                   if (isContent) {
                                     addContentChildWithNoAttribute();
                                   } else {
                                     builder.supportsAttributeDeclaration(true)
                                         .supportsChildDeclaration(false)
                                         .withAttributeName(parameter.getName());
                                   }
                                 }

                                 @Override
                                 public void visitString(StringType stringType) {
                                   // For Text parameters, we don't allow the attribute to be set
                                   if (isText(parameter) || isContent) {
                                     addContentChildWithNoAttribute();
                                   } else {
                                     builder.supportsAttributeDeclaration(true)
                                         .supportsChildDeclaration(false)
                                         .withAttributeName(parameter.getName());
                                   }
                                 }

                                 @Override
                                 public void visitArrayType(ArrayType arrayType) {
                                   defaultVisit(arrayType);
                                   builder.withNamespace(prefix.get(), namespace.get())
                                       .withElementName(elementName.get());

                                   MetadataType genericType = arrayType.getType();
                                   boolean supportsInline = supportsInlineDeclaration(arrayType, expressionSupport,
                                                                                      dslConfig, isContent);
                                   boolean requiresWrapper = typeRequiresWrapperElement(genericType, typeCatalog);
                                   if (supportsInline || requiresWrapper) {
                                     builder.supportsChildDeclaration(true);
                                     if (!isContent) {
                                       genericType.accept(getArrayItemTypeVisitor(builder, parameter.getName(),
                                                                                  prefix.get(),
                                                                                  namespace.get(),
                                                                                  false));
                                     }
                                   }
                                 }

                                 @Override
                                 public void visitObject(ObjectType objectType) {
                                   addAttributeName(builder, parameter, isContent, dslConfig);
                                   builder.withNamespace(prefix.get(), namespace.get());
                                   if (isMap(objectType)) {
                                     resolveMapDslFromParameter(objectType, builder, isContent,
                                                                expressionSupport, dslConfig, parameter.getName(),
                                                                prefix.get(), namespace.get());
                                   } else {
                                     builder.withElementName(elementName.get());

                                     final XmlDslModel importedObjectType = importedTypes.get(objectType);

                                     resolveObjectDslFromParameter(parameter, objectType, builder,
                                                                   isContent, dslConfig, expressionSupport,
                                                                   importedObjectType != null
                                                                       ? importedObjectType.getPrefix()
                                                                       : prefix.get(),
                                                                   importedObjectType != null
                                                                       ? importedObjectType.getNamespace()
                                                                       : namespace.get());
                                   }
                                 }

                                 private void addContentChildWithNoAttribute() {
                                   builder.withNamespace(prefix.get(), namespace.get())
                                       .withElementName(elementName.get())
                                       .supportsChildDeclaration(true)
                                       .supportsAttributeDeclaration(false);
                                 }

                               });
    return builder.build();
  }

  /**
   * Resolves the {@link DslElementSyntax} for a {@link ParameterGroupModel} that has to be shown as an inline element of the DSL
   *
   * @param group the {@link ParameterGroupModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterGroupModel group}
   */
  @Override
  public DslElementSyntax resolveInline(ParameterGroupModel group) {
    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
    builder.withNamespace(languageModel.getPrefix(), languageModel.getNamespace())
        .withElementName(getSanitizedElementName(group))
        .supportsAttributeDeclaration(false)
        .supportsChildDeclaration(true)
        .supportsTopLevelDeclaration(false);

    group.getParameterModels().forEach(parameter -> builder.containing(parameter.getName(), resolve(parameter)));

    return builder.build();
  }

  /**
   * Resolves the {@link DslElementSyntax} for the standalone xml element for the given {@link MetadataType}
   *
   * @param type the {@link MetadataType} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the top level element associated to the {@link MetadataType} or
   *         {@link Optional#empty} if the {@code type} is not supported as an standalone element
   */
  @Override
  public Optional<DslElementSyntax> resolve(MetadataType type) {
    return type instanceof ObjectType ? resolvePojoDsl((ObjectType) type) : empty();
  }

  private Optional<DslElementSyntax> resolvePojoDsl(ObjectType type) {

    boolean isSubtype = !typeCatalog.getSuperTypes(type).isEmpty();

    boolean requiresWrapper = typeRequiresWrapperElement(type, typeCatalog);
    boolean supportsInlineDeclaration = supportsInlineDeclaration(type, NOT_SUPPORTED) || (isInstantiable(type) && isSubtype);
    boolean supportTopLevelElement = supportTopLevelElement(type);

    if (!supportsInlineDeclaration && !supportTopLevelElement && !requiresWrapper && !isSubtype) {
      return empty();
    }

    Reference<String> prefix = new Reference<>(getPrefix(type));
    Reference<String> namespace = new Reference<>(getNamespace(type));
    Reference<String> elementName = new Reference<>(getTopLevelTypeName(type));

    getCustomQName(type).ifPresent(qName -> {
      prefix.set(qName.getPrefix());
      namespace.set(qName.getNamespaceURI());
      elementName.set(qName.getLocalPart());
    });

    final Optional<String> key = getTypeKey(type, prefix.get(), namespace.get());

    if (!key.isPresent()) {
      return empty();
    }

    if (resolvedTypes.containsKey(key.get())) {
      return of(resolvedTypes.get(key.get()));
    }

    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create()
        .withNamespace(prefix.get(), namespace.get())
        .withElementName(elementName.get())
        .supportsTopLevelDeclaration(supportTopLevelElement)
        .supportsChildDeclaration(supportsInlineDeclaration)
        .supportsAttributeDeclaration(false)
        .asWrappedElement(requiresWrapper);

    Optional<String> typeId = getTypeId(type);
    if (typeId.isPresent() && !typeResolvingStack.contains(typeId.get())) {
      if (supportTopLevelElement || supportsInlineDeclaration) {
        withStackControl(typeId.get(), () -> declareFieldsAsChilds(builder, type.getFields(), prefix.get(), namespace.get()));
      }

      DslElementSyntax dsl = builder.build();
      resolvedTypes.put(key.get(), dsl);

      return of(dsl);
    }

    return of(builder.build());
  }

  private void resolveComponentDsl(ComponentModel component, final DslElementSyntaxBuilder dsl) {
    dsl.supportsTopLevelDeclaration(false);

    resolveParameterizedDsl(component, dsl);

    component.accept(new ComponentModelVisitor() {

      @Override
      public void visit(OperationModel operationModel) {}

      @Override
      public void visit(SourceModel sourceModel) {
        sourceModel.getSuccessCallback().ifPresent(cb -> resolveParameterizedDsl(cb, dsl));
        sourceModel.getErrorCallback().ifPresent(cb -> resolveParameterizedDsl(cb, dsl));
      }

      @Override
      public void visit(ConstructModel model) {
        model.getNestedComponents().forEach(child -> child.accept(new NestableElementModelVisitor() {

          @Override
          public void visit(NestedComponentModel component) {
            // no-op
          }

          @Override
          public void visit(NestedChainModel component) {
            // no-op
          }

          @Override
          public void visit(NestedRouteModel component) {
            dsl.containing(component.getName(), resolveRouteDsl(component));
          }
        }));
      }

    });
  }

  private DslElementSyntax resolveRouteDsl(final NestedRouteModel route) {

    DslElementSyntaxBuilder dsl = DslElementSyntaxBuilder.create()
        .withElementName(getSanitizedElementName(route))
        .withNamespace(languageModel.getPrefix(), languageModel.getNamespace())
        .supportsTopLevelDeclaration(false)
        .supportsChildDeclaration(true)
        .supportsAttributeDeclaration(false)
        .requiresConfig(false);

    resolveParameterizedDsl(route, dsl);

    return dsl.build();
  }

  private void resolveObjectDslFromParameter(ParameterModel parameter, ObjectType objectType, DslElementSyntaxBuilder builder,
                                             boolean isContent, ParameterDslConfiguration dslConfig,
                                             ExpressionSupport expressionSupport,
                                             String prefix, String namespace) {

    boolean supportsTopLevel;
    boolean supportsInline;
    boolean requiresWrapper;

    if (isInfrastructure(parameter)) {
      supportsTopLevel = dslConfig.allowTopLevelDefinition();
      supportsInline = dslConfig.allowsInlineDefinition();
      requiresWrapper = isExtensible(objectType);
    } else {
      supportsTopLevel = supportTopLevelElement(objectType, dslConfig);
      supportsInline = supportsInlineDeclaration(objectType, expressionSupport, dslConfig, isContent);
      requiresWrapper = typeRequiresWrapperElement(objectType, typeCatalog);
    }

    builder.supportsTopLevelDeclaration(supportsTopLevel);
    if ((supportsInline || requiresWrapper) && dslConfig.allowsInlineDefinition()) {
      builder.supportsChildDeclaration(true);
      if (requiresWrapper) {
        builder.asWrappedElement(true);
      } else {
        if (!isContent) {
          declareFieldsAsChilds(builder, objectType.getFields(),
                                prefix, namespace);
        }
      }
    }
  }

  private void resolveMapDslFromParameter(ObjectType objectType, DslElementSyntaxBuilder builder,
                                          boolean isContent, ExpressionSupport expressionSupport,
                                          ParameterDslConfiguration dslModel,
                                          String name, String namespace, String namespaceUri) {

    final String parameterName = isContent ? name : pluralize(name);
    builder.withElementName(hyphenize(parameterName))
        .supportsChildDeclaration(supportsInlineDeclaration(objectType, expressionSupport, dslModel, isContent));

    if (!isContent) {
      objectType.getOpenRestriction()
          .ifPresent(type -> type
              .accept(getMapValueTypeVisitor(builder, name,
                                             namespace, namespaceUri,
                                             dslModel)));
    }
  }

  private void resolveParameterizedDsl(ParameterizedModel component, DslElementSyntaxBuilder dsl) {
    List<ParameterModel> inlineGroupedParameters = component.getParameterGroupModels().stream()
        .filter(ParameterGroupModel::isShowInDsl)
        .peek(group -> dsl.containing(group.getName(), resolveInline(group)))
        .flatMap(g -> g.getParameterModels().stream())
        .collect(toList());

    component.getAllParameterModels().stream()
        .filter(p -> !inlineGroupedParameters.contains(p))
        .forEach(parameter -> dsl.containing(parameter.getName(), resolve(parameter)));
  }

  private MetadataTypeVisitor getArrayItemTypeVisitor(final DslElementSyntaxBuilder listBuilder, final String parameterName,
                                                      final String namespace, final String namespaceUri, boolean asItem) {
    return new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
        if (isMap(objectType)) {
          defaultVisit(objectType);
          return;
        }

        if (typeRequiresWrapperElement(objectType, typeCatalog)) {
          listBuilder.withGeneric(objectType,
                                  DslElementSyntaxBuilder.create()
                                      .withNamespace(getPrefix(objectType), getNamespace(objectType))
                                      .withElementName(getTopLevelTypeName(objectType))
                                      .supportsAttributeDeclaration(false)
                                      .supportsChildDeclaration(supportsInlineDeclaration(objectType, NOT_SUPPORTED))
                                      .asWrappedElement(true)
                                      .supportsTopLevelDeclaration(supportTopLevelElement(objectType,
                                                                                          ParameterDslConfiguration
                                                                                              .getDefaultInstance()))
                                      .build());
        } else if (isValidBean(objectType)) {
          listBuilder.withGeneric(objectType, resolve(objectType).get());
        }
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        DslElementSyntaxBuilder genericBuilder = DslElementSyntaxBuilder.create()
            .withNamespace(namespace, namespaceUri)
            .withElementName(resolveItemName(parameterName, asItem))
            .supportsAttributeDeclaration(false);

        MetadataType genericType = arrayType.getType();

        boolean supportsInline = supportsInlineDeclaration(genericType, SUPPORTED);
        boolean requiresWrapper = typeRequiresWrapperElement(genericType, typeCatalog);
        if (supportsInline || requiresWrapper) {
          genericBuilder.supportsChildDeclaration(true);
          genericType.accept(getArrayItemTypeVisitor(genericBuilder, parameterName, namespace, namespaceUri, true));
        }

        listBuilder.withGeneric(arrayType, genericBuilder.build());
      }

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        listBuilder.withGeneric(metadataType,
                                DslElementSyntaxBuilder.create()
                                    .withNamespace(namespace, namespaceUri)
                                    .withElementName(resolveItemName(parameterName, asItem))
                                    .supportsAttributeDeclaration(false)
                                    .supportsChildDeclaration(true)
                                    .containing(VALUE_ATTRIBUTE_NAME,
                                                DslElementSyntaxBuilder.create()
                                                    .withAttributeName(VALUE_ATTRIBUTE_NAME)
                                                    .build())
                                    .build());
      }
    };
  }

  private MetadataTypeVisitor getMapValueTypeVisitor(final DslElementSyntaxBuilder mapBuilder, final String parameterName,
                                                     final String namespace, final String namespaceUri,
                                                     ParameterDslConfiguration dslModel) {
    return new MetadataTypeVisitor() {

      /**
       * Builds the default {@code map-element} with a {@code key} and {@code value} attributes:
       *
       * {@code
       * <ns:map-elements>
       *    <ns:map-element key="" value=""/>
       * </ns:map-elements>
       * }
       */
      @Override
      protected void defaultVisit(MetadataType metadataType) {
        mapBuilder.withGeneric(metadataType,
                               createBaseValueEntryDefinition()
                                   .containing(VALUE_ATTRIBUTE_NAME, DslElementSyntaxBuilder.create()
                                       .withAttributeName(VALUE_ATTRIBUTE_NAME).build())
                                   .build());
      }

      /**
       * Builds the {@code map-element} that allows to represent complex objects both as a {@code value} attribute or as an inline
       * definition if the given {@code objectType} supports it.
       *
       * Value attribute representation: {@code
       * <ns:map-elements>
       *    <ns:map-element key="one" value="#[myPojoVar]"/>
       * </ns:map-elements>
       * }
       *
       * Inline object representation: {@code
       * <ns:map-elements>
       *    <ns:map-element key="one">
       *        <ns:complex-type-name attr="">
       *          ...
       *        <ns:complex-type-name>
       *    </ns:map-element>
       * </ns:map-elements>
       * }
       *
       * For {@link Map} open objects, it delegates to {@link this#defaultVisit}.
       */
      @Override
      public void visitObject(ObjectType objectType) {
        if (isMap(objectType)) {
          defaultVisit(objectType);
          return;
        }

        boolean supportsInlineDeclaration = supportsInlineDeclaration(objectType, SUPPORTED, dslModel, false);
        boolean requiresWrapperElement = typeRequiresWrapperElement(objectType, typeCatalog);

        if (supportsInlineDeclaration || requiresWrapperElement) {
          final DslElementSyntaxBuilder valueEntry = createBaseValueEntryDefinition();

          final String namespace = getPrefix(objectType);
          final String namespaceUri = getNamespace(objectType);

          final DslElementSyntaxBuilder innerPojoDsl = DslElementSyntaxBuilder.create()
              .withAttributeName(VALUE_ATTRIBUTE_NAME)
              .withNamespace(namespace, namespaceUri);

          if (supportsInlineDeclaration) {
            innerPojoDsl.withElementName(getTopLevelTypeName(objectType))
                .supportsChildDeclaration(true);

            getId(objectType)
                .ifPresent(id -> withStackControl(id, () -> declareFieldsAsChilds(innerPojoDsl, objectType.getFields(), namespace,
                                                                                  namespaceUri)));
          } else {
            innerPojoDsl.asWrappedElement(true);
          }

          valueEntry.containing(VALUE_ATTRIBUTE_NAME, innerPojoDsl.build());

          mapBuilder.withGeneric(objectType, valueEntry.build());
        } else {
          defaultVisit(objectType);
        }
      }

      /**
       * Builds the {@code map-element} that allows to represent a list of elements both as a {@code value} attribute or as an
       * inline definition.
       *
       * Value attribute representation: {@code
       * <ns:map-elements>
       *    <ns:map-element key="one" value="#[myListVar]"/>
       * </ns:map-elements>
       * }
       *
       * Inline list representation: {@code
       * <ns:map-elements>
       *    <ns:map-element key="">
       *        <ns:map-element-item value="one"/>
       *        <ns:map-element-item value="two"/>
       *    </ns:map-element>
       * </ns:map-elements>
       * }
       *
       * List items may also be complex elements like objects or nested lists, in that case the {@code value} attribute of the
       * item is replaced by inline content inside the {@code map-element-item} entry.
       */
      @Override
      public void visitArrayType(ArrayType arrayType) {
        final DslElementSyntaxBuilder valueEntry = createBaseValueEntryDefinition()
            .containing(VALUE_ATTRIBUTE_NAME, DslElementSyntaxBuilder.create().withAttributeName(VALUE_ATTRIBUTE_NAME).build());

        MetadataType genericType = arrayType.getType();
        boolean genericSupportsInline = supportsInlineDeclaration(genericType, SUPPORTED, dslModel, false);
        boolean genericRequiresWrapper = typeRequiresWrapperElement(genericType, typeCatalog);
        if (genericSupportsInline || genericRequiresWrapper) {
          genericType.accept(getArrayItemTypeVisitor(valueEntry, parameterName, namespace, namespaceUri, true));
        }

        mapBuilder.withGeneric(arrayType, valueEntry.build());
      }

      private DslElementSyntaxBuilder createBaseValueEntryDefinition() {
        return DslElementSyntaxBuilder.create()
            .withNamespace(namespace, namespaceUri)
            .withElementName(hyphenize(singularize(parameterName)))
            .supportsAttributeDeclaration(false)
            .supportsChildDeclaration(true)
            .containing(KEY_ATTRIBUTE_NAME, DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE_NAME).build());
      }
    };
  }

  private void addBeanDeclarationSupport(ObjectType objectType, Collection<ObjectFieldType> childFields,
                                         DslElementSyntaxBuilder builder, String namespace, String namespaceUri,
                                         boolean introspectObjectFields) {

    boolean supportsChildDeclaration = supportsInlineDeclaration(objectType, SUPPORTED);
    boolean supportsTopDeclaration = supportTopLevelElement(objectType);

    builder.supportsChildDeclaration(supportsChildDeclaration)
        .supportsTopLevelDeclaration(supportsTopDeclaration)
        .asWrappedElement(typeRequiresWrapperElement(objectType, typeCatalog));

    if (introspectObjectFields && (supportsChildDeclaration || supportsTopDeclaration)) {
      declareFieldsAsChilds(builder, childFields, namespace, namespaceUri);
    }
  }

  private MetadataTypeVisitor getObjectFieldVisitor(final DslElementSyntaxBuilder objectFieldBuilder,
                                                    final String fieldName,
                                                    final String fieldElementName,
                                                    final String ownerNamespace,
                                                    final String ownerNamespaceUri) {
    return new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        objectFieldBuilder
            .withAttributeName(fieldName)
            .supportsAttributeDeclaration(true)
            .supportsTopLevelDeclaration(false)
            .supportsChildDeclaration(false)
            .requiresConfig(false);
      }

      @Override
      public void visitUnion(UnionType unionType) {
        objectFieldBuilder.withElementName(fieldElementName)
            .withNamespace(getPrefix(unionType, ownerNamespace),
                           getNamespace(unionType, ownerNamespaceUri))
            .supportsAttributeDeclaration(false)
            .supportsTopLevelDeclaration(false)
            .supportsChildDeclaration(true)
            .requiresConfig(false);

        final boolean extensible = isExtensible(unionType);
        objectFieldBuilder.asWrappedElement(extensible);

        unionType.getTypes().forEach(unionTypeItem -> {
          String typeId = getId(unionTypeItem).orElse(null);

          if (typeId != null) {
            DslElementSyntaxBuilder unionTypeItemBuilder = DslElementSyntaxBuilder.create();
            unionTypeItem.accept(getObjectFieldVisitor(unionTypeItemBuilder, typeId, typeId, ownerNamespace,
                                                       ownerNamespaceUri));
            objectFieldBuilder.containing(typeId, unionTypeItemBuilder.build());
          }
        });
      }

      @Override
      public void visitObject(ObjectType objectType) {
        objectFieldBuilder.withAttributeName(fieldName);
        if (isMap(objectType)) {
          handleMapObject(objectType);
        } else {
          objectFieldBuilder.withElementName(fieldElementName)
              .withNamespace(getPrefix(objectType, ownerNamespace),
                             getNamespace(objectType, ownerNamespaceUri));

          String typeId = getId(objectType).orElse(null);
          if (typeId != null && !typeResolvingStack.contains(typeId)) {
            withStackControl(typeId, () -> {
              List<ObjectFieldType> fields = objectType.getFields().stream()
                  .filter(f -> getId(f.getValue())
                      .map(id -> !typeResolvingStack.contains(id))
                      .orElse(true))
                  .collect(toList());
              addBeanDeclarationSupport(objectType, fields, objectFieldBuilder, ownerNamespace, ownerNamespaceUri, true);
            });
          } else {
            addBeanDeclarationSupport(objectType, emptyList(), objectFieldBuilder, ownerNamespace, ownerNamespaceUri, false);
          }
        }
      }

      private void handleMapObject(ObjectType objectType) {
        objectFieldBuilder.withElementName(hyphenize(pluralize(fieldName)))
            .withNamespace(ownerNamespace, ownerNamespaceUri);

        objectFieldBuilder.supportsChildDeclaration(true);

        objectFieldBuilder.withGeneric(typeLoader.load(String.class),
                                       DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE_NAME).build());
        objectType.getOpenRestriction()
            .ifPresent(type -> type.accept(getMapValueTypeVisitor(objectFieldBuilder, fieldName,
                                                                  ownerNamespace, ownerNamespaceUri,
                                                                  ParameterDslConfiguration
                                                                      .getDefaultInstance())));
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        objectFieldBuilder.withAttributeName(fieldName)
            .withElementName(fieldElementName)
            .withNamespace(ownerNamespace, ownerNamespaceUri);

        MetadataType genericType = arrayType.getType();
        if (supportsInlineDeclaration(genericType, SUPPORTED) || isBaseType(genericType)) {
          objectFieldBuilder.supportsChildDeclaration(true);
          genericType.accept(getArrayItemTypeVisitor(objectFieldBuilder, fieldName, ownerNamespace, ownerNamespaceUri, false));
        }
      }
    };
  }

  private boolean isBaseType(MetadataType type) {
    Reference<Boolean> isBaseType = new Reference<>(false);
    type.accept(new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
        isBaseType.set(typeCatalog.containsBaseType(objectType));
      }
    });
    return isBaseType.get();
  }

  private void declareFieldsAsChilds(final DslElementSyntaxBuilder objectBuilder, Collection<ObjectFieldType> fields,
                                     final String namespace, final String namespaceUri) {
    final List<ObjectFieldType> sortedFields = new ArrayList<>(fields);
    sort(sortedFields, (o1, o2) -> o1.getAnnotation(LayoutTypeAnnotation.class).flatMap(LayoutTypeAnnotation::getOrder).orElse(-1)
        - o2.getAnnotation(LayoutTypeAnnotation.class).flatMap(LayoutTypeAnnotation::getOrder).orElse(-1));

    sortedFields.forEach(
                         field -> {
                           DslElementSyntaxBuilder fieldBuilder = DslElementSyntaxBuilder.create();
                           String childName = field.getKey().getName().getLocalPart();
                           final MetadataType fieldValue = field.getValue();

                           Reference<String> fieldPrefix = new Reference<>(namespace);
                           Reference<String> fieldNamespaceUri = new Reference<>(namespaceUri);
                           Reference<String> elementName = new Reference<>(hyphenize(childName));

                           if (isFlattened(field, fieldValue)) {
                             declareFieldsAsChilds(objectBuilder, ((ObjectType) fieldValue).getFields(),
                                                   fieldPrefix.get(), fieldNamespaceUri.get());
                           } else {
                             if (isText(field)) {
                               fieldBuilder
                                   .supportsAttributeDeclaration(false)
                                   .supportsChildDeclaration(true)
                                   .withElementName(elementName.get())
                                   .withNamespace(fieldPrefix.get(), fieldNamespaceUri.get());
                             } else {

                               getCustomQName(fieldValue).ifPresent(qName -> {
                                 elementName.set(qName.getLocalPart());
                                 fieldPrefix.set(qName.getPrefix());
                                 fieldNamespaceUri.set(qName.getNamespaceURI());
                               });

                               fieldValue
                                   .accept(getObjectFieldVisitor(fieldBuilder, childName,
                                                                 elementName.get(), fieldPrefix.get(), fieldNamespaceUri.get()));
                               fieldBuilder.supportsAttributeDeclaration(supportAttributeDeclaration(field));
                             }

                             objectBuilder.containing(childName, fieldBuilder.build());
                           }
                         });
  }

  private void addAttributeName(DslElementSyntaxBuilder builder, ParameterModel parameter,
                                boolean isContent, ParameterDslConfiguration dslModel) {

    if (supportsAttributeDeclaration(parameter, isContent, dslModel)) {
      builder.withAttributeName(parameter.getName());
    } else {
      builder.supportsAttributeDeclaration(false);
    }
  }

  private boolean supportsAttributeDeclaration(ParameterModel parameter, boolean isContent, ParameterDslConfiguration dslModel) {
    return !isContent && (dslModel.allowsReferences() || !NOT_SUPPORTED.equals(parameter.getExpressionSupport()));
  }

  private String getPrefix(MetadataType type) {
    return getPrefix(type, languageModel.getPrefix());
  }

  private String getPrefix(MetadataType type, String prefix) {
    XmlDslModel originXml = lookupOriginXml(type);
    return originXml != null ? originXml.getPrefix() : prefix;
  }

  private String getNamespace(MetadataType type) {
    return getNamespace(type, languageModel.getNamespace());
  }

  private String getNamespace(MetadataType type, String defaultNamespace) {
    XmlDslModel originXml = lookupOriginXml(type);
    return originXml != null ? originXml.getNamespace() : defaultNamespace;
  }

  private XmlDslModel lookupOriginXml(MetadataType type) {
    return getTypeId(type)
        .flatMap(id -> typeCatalog.getType(id))
        .map(normalizedType -> importedTypes.get(normalizedType))
        .orElse(importedTypes.get(type));
  }

  private String resolveItemName(String parameterName, boolean forceItemize) {
    String singularizedName = singularize(parameterName);
    return forceItemize || parameterName.equals(singularizedName) ? itemize(singularizedName)
        : hyphenize(singularizedName);
  }

  private TypeCatalog getTypeCatalog(ExtensionModel model, DslResolvingContext context) {
    return context.getExtension(model.getName()).isPresent()
        ? context.getTypeCatalog()
        : TypeCatalog.getDefault(ImmutableSet.<ExtensionModel>builder()
            .add(model).addAll(context.getExtensions()).build());
  }

  private void withStackControl(String stackId, Runnable action) {
    if (!typeResolvingStack.contains(stackId)) {
      typeResolvingStack.push(stackId);
      action.run();
      typeResolvingStack.pop();
    }
  }

  private Optional<QName> getCustomQName(ParameterModel parameter) {
    return parameter.getModelProperty(QNameModelProperty.class).map(QNameModelProperty::getValue);
  }

  private Optional<QName> getCustomQName(MetadataType type) {
    return type.getAnnotation(QNameTypeAnnotation.class).map(QNameTypeAnnotation::getValue);
  }
}
