/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsl.syntax;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.dsl.DslConstants.KEY_ATTRIBUTE_NAME;
import static org.mule.runtime.api.dsl.DslConstants.VALUE_ATTRIBUTE_NAME;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.isContent;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.requiresConfig;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.sanitizeName;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.getId;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.getTypeKey;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.isExtensible;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.isFlattened;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.isText;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.isValidBean;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.supportTopLevelElement;
import static org.mule.runtime.extension.internal.dsl.syntax.DslSyntaxUtils.supportsInlineDeclaration;
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
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.connection.HasConnectionProviderModels;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DefaultImportTypesStrategy;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.dsl.syntax.resolver.ImportTypesStrategy;
import org.mule.runtime.extension.internal.property.QNameModelProperty;

import com.google.common.collect.ImmutableSet;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
   *                                  present in the provided {@link DslResolvingContext} or if the imported {@link ExtensionModel} doesn't have any
   *                                  {@link ImportedTypeModel}
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
   * @param model               the {@link ExtensionModel} that provides context for resolving the component's {@link DslElementSyntax}
   * @param importTypesStrategy the {@link ImportTypesStrategy} used for external types resolution
   * @return the default implementation of a {@link DslSyntaxResolver}
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *                                  present in the provided {@link DslResolvingContext} or if the imported {@link ExtensionModel} doesn't have any
   *                                  {@link ImportedTypeModel}
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
  public DslElementSyntax resolve(final NamedObject component) {
    DslElementSyntaxBuilder dsl = DslElementSyntaxBuilder.create()
        .withElementName(hyphenize(sanitizeName(component.getName())).replaceAll("\\s+", ""))
        .withNamespace(languageModel.getNamespace(), languageModel.getNamespaceUri())
        .supportsTopLevelDeclaration(true)
        .supportsChildDeclaration(true)
        .supportsAttributeDeclaration(false)
        .requiresConfig(requiresConfig(extensionModel, component));

    if (component instanceof ParameterizedModel) {
      resolveGroupsDsl((ParameterizedModel) component, dsl);
    }

    if (component instanceof HasConnectionProviderModels) {
      ((HasConnectionProviderModels) component).getConnectionProviders()
          .forEach(c -> dsl.containing(c.getName(), resolve(c)));
    }

    return dsl.build();
  }

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link ParameterModel parameter}, providing all the required information
   * for representing this {@code parameter} element in the DSL.
   *
   * @param parameter the {@link ParameterModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterModel parameter}
   */
  public DslElementSyntax resolve(final ParameterModel parameter) {
    final ExpressionSupport expressionSupport = parameter.getExpressionSupport();
    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
    final ParameterDslConfiguration dslConfig = parameter.getDslConfiguration();

    Reference<String> namespace = new Reference<>(languageModel.getNamespace());
    Reference<String> namespaceUri = new Reference<>(languageModel.getNamespaceUri());
    Reference<String> elementName = new Reference<>(hyphenize(parameter.getName()));

    parameter.getModelProperty(QNameModelProperty.class).map(QNameModelProperty::getValue).ifPresent(qName -> {
      elementName.set(qName.getLocalPart());
      namespace.set(qName.getPrefix());
      namespaceUri.set(qName.getNamespaceURI());
    });

    final boolean isContent = isContent(parameter);
    parameter.getType().accept(
                               new MetadataTypeVisitor() {

                                 @Override
                                 public void visitUnion(UnionType unionType) {
                                   unionType.getTypes().forEach(type -> type.accept(this));
                                 }

                                 @Override
                                 protected void defaultVisit(MetadataType metadataType) {
                                   addAttributeName(builder, parameter, isContent, dslConfig);
                                 }

                                 @Override
                                 public void visitString(StringType stringType) {
                                   // For Text parameters, we don't allow the attribute to be set
                                   if (isText(parameter)) {
                                     builder.withNamespace(namespace.get(), namespaceUri.get())
                                         .withElementName(elementName.get())
                                         .supportsChildDeclaration(true)
                                         .supportsAttributeDeclaration(false);
                                   } else {
                                     builder.supportsAttributeDeclaration(true)
                                         .supportsChildDeclaration(false)
                                         .withAttributeName(parameter.getName());
                                   }
                                 }

                                 @Override
                                 public void visitArrayType(ArrayType arrayType) {
                                   defaultVisit(arrayType);
                                   builder.withNamespace(namespace.get(), namespaceUri.get())
                                       .withElementName(elementName.get());

                                   MetadataType genericType = arrayType.getType();
                                   boolean supportsInline = supportsInlineDeclaration(arrayType, expressionSupport,
                                                                                      dslConfig, isContent);
                                   boolean requiresWrapper = typeRequiresWrapperElement(genericType);
                                   if (supportsInline || requiresWrapper) {
                                     builder.supportsChildDeclaration(true);
                                     if (!isContent) {
                                       genericType.accept(getArrayItemTypeVisitor(builder, parameter.getName(),
                                                                                  namespace.get(),
                                                                                  namespaceUri.get(),
                                                                                  false));
                                     }
                                   }
                                 }

                                 @Override
                                 public void visitObject(ObjectType objectType) {
                                   addAttributeName(builder, parameter, isContent, dslConfig);
                                   builder.withNamespace(namespace.get(), namespaceUri.get());
                                   if (isMap(objectType)) {
                                     resolveMapDsl(objectType, builder, isContent, expressionSupport, dslConfig,
                                                   parameter.getName(), namespace.get(), namespaceUri.get());
                                   } else {
                                     builder.withNamespace(namespace.get(), namespaceUri.get())
                                         .withElementName(elementName.get());

                                     resolveObjectDsl(objectType, builder, isContent, dslConfig, expressionSupport);
                                   }
                                 }
                               });
    return builder.build();
  }

  /**
   * Resolves the {@link DslElementSyntax} for a {@link ParameterGroupModel} that has
   * to be shown as an inline element of the DSL
   *
   * @param group the {@link ParameterGroupModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterGroupModel group}
   */
  public DslElementSyntax resolveInline(ParameterGroupModel group) {

    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
    builder.withNamespace(languageModel.getNamespace(), languageModel.getNamespaceUri())
        .withElementName(hyphenize(sanitizeName(group.getName())).replaceAll("\\s+", ""))
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
   * {@link Optional#empty} if the {@code type} is not supported as an standalone element
   */
  public Optional<DslElementSyntax> resolve(MetadataType type) {
    return type instanceof ObjectType ? resolvePojoDsl((ObjectType) type) : Optional.empty();
  }

  private Optional<DslElementSyntax> resolvePojoDsl(ObjectType type) {

    boolean requiresWrapper = typeRequiresWrapperElement(type);
    boolean supportsInlineDeclaration = supportsInlineDeclaration(type, NOT_SUPPORTED);
    boolean supportTopLevelElement = supportTopLevelElement(type);

    if (!supportsInlineDeclaration && !supportTopLevelElement
        && !requiresWrapper && typeCatalog.getSuperTypes(type).isEmpty()) {
      return Optional.empty();
    }

    final String namespace = getNamespace(type);
    final String namespaceUri = getNamespaceUri(type);

    final String key = getTypeKey(type, namespace, namespaceUri);

    if (resolvedTypes.containsKey(key)) {
      return Optional.of(resolvedTypes.get(key));
    }

    final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
    builder.withNamespace(namespace, namespaceUri)
        .withElementName(getTopLevelTypeName(type))
        .supportsTopLevelDeclaration(supportTopLevelElement)
        .supportsChildDeclaration(supportsInlineDeclaration)
        .supportsAttributeDeclaration(false)
        .asWrappedElement(requiresWrapper);

    String typeId = getId(type);
    if (!typeResolvingStack.contains(typeId)) {
      if (supportTopLevelElement || supportsInlineDeclaration) {
        typeResolvingStack.push(typeId);
        declareFieldsAsChilds(builder, type.getFields(), namespace, namespaceUri);
        typeResolvingStack.pop();
      }

      DslElementSyntax dsl = builder.build();
      resolvedTypes.put(key, dsl);

      return Optional.of(dsl);
    }

    return Optional.of(builder.build());
  }

  private void resolveObjectDsl(ObjectType objectType, DslElementSyntaxBuilder builder,
                                boolean isContent, ParameterDslConfiguration dslModel, ExpressionSupport expressionSupport) {

    builder.supportsTopLevelDeclaration(supportTopLevelElement(objectType, dslModel));

    boolean shouldGenerateChild = supportsInlineDeclaration(objectType, expressionSupport, dslModel, isContent);
    boolean requiresWrapper = typeRequiresWrapperElement(objectType);
    if (shouldGenerateChild || requiresWrapper) {
      builder.supportsChildDeclaration(true);
      if (requiresWrapper) {
        builder.asWrappedElement(true);
      } else {
        if (!isContent) {
          declareFieldsAsChilds(builder, objectType.getFields(),
                                languageModel.getNamespace(), languageModel.getNamespaceUri());
        }
      }
    }
  }

  private void resolveMapDsl(ObjectType objectType, DslElementSyntaxBuilder builder,
                             boolean isContent, ExpressionSupport expressionSupport,
                             ParameterDslConfiguration dslModel,
                             String name, String namespace, String namespaceUri) {
    final String parameterName =
        isContent ? name : pluralize(name);
    builder.withElementName(hyphenize(parameterName))
        .supportsChildDeclaration(supportsInlineDeclaration(objectType,
                                                            expressionSupport,
                                                            isContent));
    if (!isContent) {
      builder.withGeneric(typeLoader.load(String.class),
                          DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE_NAME)
                              .build());
      objectType.getOpenRestriction()
          .ifPresent(type -> type
              .accept(getMapValueTypeVisitor(builder, name,
                                             namespace, namespaceUri,
                                             dslModel)));
    }
  }

  private void resolveGroupsDsl(ParameterizedModel component, DslElementSyntaxBuilder dsl) {
    List<ParameterModel> inlineGroupedParameters = component.getParameterGroupModels().stream()
        .filter(ParameterGroupModel::isShowInDsl)
        .peek(group -> dsl.containing(group.getName(), resolveInline(group)))
        .flatMap(g -> g.getParameterModels().stream())
        .collect(toList());

    component.getAllParameterModels().stream()
        .filter(p -> !inlineGroupedParameters.contains(p))
        .forEach(parameter -> dsl.containing(parameter.getName(), resolve(parameter)));
  }

  private void addAttributeName(DslElementSyntaxBuilder builder, ParameterModel parameter,
                                boolean isContent, ParameterDslConfiguration dslModel) {

    if (supportsAttributeDeclaration(parameter, isContent, dslModel)) {
      builder.withAttributeName(parameter.getName());
    } else {
      // For Content parameters, we don't allow the attribute to be set
      builder.supportsAttributeDeclaration(false);
    }
  }

  private boolean supportsAttributeDeclaration(ParameterModel parameter, boolean isContent, ParameterDslConfiguration dslModel) {
    return !isContent && (dslModel.allowsReferences() || !NOT_SUPPORTED.equals(parameter.getExpressionSupport()));
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

        if (typeRequiresWrapperElement(objectType)) {
          listBuilder.withGeneric(objectType,
                                  DslElementSyntaxBuilder.create()
                                      .withNamespace(getNamespace(objectType), getNamespaceUri(objectType))
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
        boolean requiresWrapper = typeRequiresWrapperElement(genericType);
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

      @Override
      public void visitObject(ObjectType objectType) {
        if (isMap(objectType)) {
          defaultVisit(objectType);
          return;
        }

        boolean supportsInlineDeclaration = supportsInlineDeclaration(objectType, SUPPORTED, dslModel, false);
        boolean requiresWrapperElement = typeRequiresWrapperElement(objectType);

        if (supportsInlineDeclaration || requiresWrapperElement) {

          DslElementSyntaxBuilder builder = createBaseValueDefinition();
          if (!typeResolvingStack.contains(getId(objectType))) {
            typeResolvingStack.push(getId(objectType));
            addBeanDeclarationSupport(objectType, objectType.getFields(), builder, namespace, namespaceUri, true);
            typeResolvingStack.pop();
          } else {
            addBeanDeclarationSupport(objectType, emptyList(), builder, namespace, namespaceUri, false);
          }

          builder.supportsChildDeclaration(true);
          builder.asWrappedElement(requiresWrapperElement);

          mapBuilder.withGeneric(objectType, builder.build());
        } else {
          defaultVisit(objectType);
        }
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        DslElementSyntaxBuilder listBuilder = createBaseValueDefinition();

        MetadataType genericType = arrayType.getType();
        boolean supportsInline = supportsInlineDeclaration(genericType, SUPPORTED, dslModel, false);
        boolean requiresWrapper = typeRequiresWrapperElement(genericType);
        if (supportsInline || requiresWrapper) {
          listBuilder.supportsChildDeclaration(true);
          genericType.accept(getArrayItemTypeVisitor(listBuilder, parameterName, namespace, namespaceUri, true));
        }

        mapBuilder.withGeneric(arrayType, listBuilder.build());
      }

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        mapBuilder.withGeneric(metadataType, createBaseValueDefinition().build());
      }

      private DslElementSyntaxBuilder createBaseValueDefinition() {
        return DslElementSyntaxBuilder.create()
            .withAttributeName(VALUE_ATTRIBUTE_NAME)
            .withNamespace(namespace, namespaceUri)
            .withElementName(hyphenize(singularize(parameterName)))
            .supportsAttributeDeclaration(true);
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
        .asWrappedElement(typeRequiresWrapperElement(objectType));

    if (introspectObjectFields && (supportsChildDeclaration || supportsTopDeclaration)) {
      declareFieldsAsChilds(builder, childFields, namespace, namespaceUri);
    }
  }

  private MetadataTypeVisitor getObjectFieldVisitor(final DslElementSyntaxBuilder objectFieldBuilder,
                                                    final boolean isText,
                                                    final String fieldName,
                                                    final String ownerNamespace,
                                                    final String ownerNamespaceUri) {
    return new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        objectFieldBuilder.withAttributeName(fieldName);
      }

      @Override
      public void visitString(StringType stringType) {
        if (isText) {
          objectFieldBuilder
              .supportsAttributeDeclaration(false)
              .supportsChildDeclaration(true)
              .withElementName(hyphenize(fieldName))
              .withNamespace(ownerNamespace, ownerNamespaceUri);
        } else {
          defaultVisit(stringType);
        }
      }

      @Override
      public void visitObject(ObjectType objectType) {
        objectFieldBuilder.withAttributeName(fieldName);
        if (isMap(objectType)) {
          objectFieldBuilder.withElementName(hyphenize(pluralize(fieldName)))
              .withNamespace(ownerNamespace, ownerNamespaceUri);

          objectFieldBuilder.supportsChildDeclaration(true);

          objectFieldBuilder.withGeneric(typeLoader.load(String.class),
                                         DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE_NAME).build());
          objectFieldBuilder.withAttributeName(KEY_ATTRIBUTE_NAME);
          objectType.getOpenRestriction()
              .ifPresent(type -> type.accept(getMapValueTypeVisitor(objectFieldBuilder, fieldName,
                                                                    ownerNamespace, ownerNamespaceUri,
                                                                    ParameterDslConfiguration
                                                                        .getDefaultInstance())));
        } else {
          objectFieldBuilder.withElementName(hyphenize(fieldName))
              .withNamespace(getNamespace(objectType, ownerNamespace), getNamespaceUri(objectType, ownerNamespaceUri));

          if (!typeResolvingStack.contains(getId(objectType))) {
            typeResolvingStack.push(getId(objectType));

            List<ObjectFieldType> fields = objectType.getFields().stream()
                .filter(f -> !typeResolvingStack.contains(getId(f.getValue())))
                .collect(toList());

            addBeanDeclarationSupport(objectType, fields, objectFieldBuilder, ownerNamespace, ownerNamespaceUri, true);

            typeResolvingStack.pop();
          } else {
            addBeanDeclarationSupport(objectType, emptyList(), objectFieldBuilder, ownerNamespace, ownerNamespaceUri, false);
          }
        }
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        objectFieldBuilder.withAttributeName(fieldName)
            .withElementName(hyphenize(fieldName))
            .withNamespace(ownerNamespace, ownerNamespaceUri);

        MetadataType genericType = arrayType.getType();
        if (supportsInlineDeclaration(genericType, SUPPORTED)) {
          objectFieldBuilder.supportsChildDeclaration(true);
          genericType.accept(getArrayItemTypeVisitor(objectFieldBuilder, fieldName, ownerNamespace, ownerNamespaceUri, false));
        }
      }
    };
  }

  private void declareFieldsAsChilds(final DslElementSyntaxBuilder objectBuilder, Collection<ObjectFieldType> fields,
                                     final String namespace, final String namespaceUri) {
    fields.forEach(
                   field -> {
                     DslElementSyntaxBuilder fieldBuilder = DslElementSyntaxBuilder.create();
                     String childName = field.getKey().getName().getLocalPart();
                     final MetadataType fieldValue = field.getValue();

                     if (isFlattened(field, fieldValue)) {
                       declareFieldsAsChilds(objectBuilder, ((ObjectType) fieldValue).getFields(), namespace, namespaceUri);
                     } else {
                       if (isContent(field)) {
                         fieldBuilder
                             .supportsAttributeDeclaration(false)
                             .supportsChildDeclaration(true)
                             .withElementName(hyphenize(childName))
                             .withNamespace(namespace, namespaceUri);
                       } else {
                         fieldValue
                             .accept(getObjectFieldVisitor(fieldBuilder, isText(field), childName, namespace, namespaceUri));
                       }

                       objectBuilder.containing(childName, fieldBuilder.build());
                     }
                   });
  }

  private boolean typeRequiresWrapperElement(MetadataType metadataType) {
    return metadataType instanceof ObjectType &&
        (isExtensible(metadataType) || typeCatalog.containsBaseType((ObjectType) metadataType));
  }

  private String getNamespace(MetadataType type) {
    return getNamespace(type, languageModel.getNamespace());
  }

  private String getNamespace(MetadataType type, String defaultNamespace) {
    XmlDslModel originXml = importedTypes.get(type);
    return originXml != null ? originXml.getNamespace() : defaultNamespace;
  }

  private String getNamespaceUri(MetadataType type) {
    return getNamespaceUri(type, languageModel.getNamespaceUri());
  }

  private String getNamespaceUri(MetadataType type, String defaultUri) {
    XmlDslModel originXml = importedTypes.get(type);
    return originXml != null ? originXml.getNamespaceUri() : defaultUri;
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
}
