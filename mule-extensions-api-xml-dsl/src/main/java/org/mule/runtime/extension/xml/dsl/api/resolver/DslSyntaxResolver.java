/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.requiresConfig;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.getId;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.getTypeKey;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.isExtensible;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.isFlattened;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.isText;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.isValidBean;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.loadSubTypes;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.supportTopLevelElement;
import static org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxUtils.supportsInlineDeclaration;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.TypeUtils;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.internal.DslElementSyntaxBuilder;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides the {@link DslElementSyntax} of any {@link NamedObject Component}, {@link ParameterModel Parameter} or
 * {@link MetadataType Type} within the context of the {@link ExtensionModel Extension model} where the Component was declared.
 *
 * @since 1.0
 */
public class DslSyntaxResolver {

  private static final String VALUE_ATTRIBUTE = "value";
  private static final String KEY_ATTRIBUTE = "key";
  private final SubTypesMappingContainer subTypesMap;
  private final XmlDslModel languageModel;
  private final Map<String, DslElementSyntax> resolvedTypes = new HashMap<>();
  private final Map<MetadataType, XmlDslModel> importedTypes;
  private final Deque<String> typeResolvingStack = new ArrayDeque<>();

  /**
   * @param model the {@link ExtensionModel} that provides context for resolving the component's {@link DslElementSyntax}
   * @param context the {@link DslResolvingContext} in which the Dsl resolution takes place
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *         present in the provided {@link DslResolvingContext} or if the imported {@link ExtensionModel} doesn't have any
   *         {@link ImportedTypeModel}
   */
  public DslSyntaxResolver(ExtensionModel model, DslResolvingContext context) {
    this(model, new DefaultImportTypesStrategy(model, context));
  }

  public DslSyntaxResolver(ExtensionModel model, ImportTypesStrategy importTypesStrategy) {
    this.languageModel = model.getXmlDslModel();
    this.subTypesMap = loadSubTypes(model);
    this.importedTypes = importTypesStrategy.getImportedTypes();
  }

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link NamedObject component}.
   *
   * @param component the {@link NamedObject} element to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link NamedObject model}
   */
  public DslElementSyntax resolve(final NamedObject component) {
    return DslElementSyntaxBuilder.create()
        .withElementName(hyphenize(component.getName()))
        .withNamespace(languageModel.getNamespace(), languageModel.getNamespaceUri())
        .requiresConfig(requiresConfig(component))
        .build();
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
    final String namespace = getNamespace(parameter.getType());
    final String namespaceUri = getNamespaceUri(parameter.getType());
    final ElementDslModel dslModel = parameter.getDslModel();
    final boolean isContent = isContent(parameter);

    parameter.getType().accept(
                               new MetadataTypeVisitor() {

                                 @Override
                                 public void visitUnion(UnionType unionType) {
                                   unionType.getTypes().forEach(type -> type.accept(this));
                                 }

                                 @Override
                                 protected void defaultVisit(MetadataType metadataType) {
                                   builder.withNamespace(namespace, namespaceUri).withElementName(hyphenize(parameter.getName()));

                                   if (!isContent) {
                                     builder.withAttributeName(parameter.getName());
                                   }

                                 }

                                 @Override
                                 public void visitString(StringType stringType) {
                                   defaultVisit(stringType);
                                   builder.supportsChildDeclaration(isText(parameter));
                                 }

                                 @Override
                                 public void visitArrayType(ArrayType arrayType) {
                                   defaultVisit(arrayType);
                                   MetadataType genericType = arrayType.getType();
                                   boolean supportsInline = supportsInlineDeclaration(arrayType, expressionSupport,
                                                                                      dslModel, isContent);
                                   boolean requiresWrapper = typeRequiresWrapperElement(genericType);
                                   if (supportsInline || requiresWrapper) {
                                     builder.supportsChildDeclaration(true);
                                     if (!isContent) {
                                       genericType.accept(getArrayItemTypeVisitor(builder, parameter.getName(), namespace,
                                                                                  namespaceUri, false));
                                     }
                                   }
                                 }

                                 @Override
                                 public void visitObject(ObjectType objectType) {
                                   builder.withAttributeName(parameter.getName())
                                       .withNamespace(namespace, namespaceUri)
                                       .withElementName(hyphenize(parameter.getName()))
                                       .supportsTopLevelDeclaration(supportTopLevelElement(objectType, dslModel));

                                   boolean shouldGenerateChild =
                                       supportsInlineDeclaration(objectType, expressionSupport, dslModel, isContent);
                                   boolean requiresWrapper = typeRequiresWrapperElement(objectType);
                                   if (shouldGenerateChild || requiresWrapper) {
                                     builder.supportsChildDeclaration(true);
                                     if (requiresWrapper) {
                                       builder.asWrappedElement(true)
                                           .withNamespace(namespace, namespaceUri);
                                     } else {
                                       builder.withNamespace(languageModel.getNamespace(), languageModel.getNamespaceUri());
                                     }

                                     if (!isContent) {
                                       declareFieldsAsChilds(builder, objectType.getFields(), namespace, namespaceUri);
                                     }
                                   }
                                 }

                                 @Override
                                 public void visitDictionary(DictionaryType dictionaryType) {
                                   builder.withAttributeName(parameter.getName())
                                       .withNamespace(namespace, namespaceUri)
                                       .withElementName(hyphenize(pluralize(parameter.getName())))
                                       .supportsChildDeclaration(supportsInlineDeclaration(dictionaryType,
                                                                                           expressionSupport,
                                                                                           isContent));
                                   if (!isContent) {
                                     builder.withGeneric(dictionaryType.getKeyType(),
                                                         DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE)
                                                             .build());

                                     dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(builder,
                                                                                                        parameter.getName(),
                                                                                                        namespace, namespaceUri,
                                                                                                        dslModel));
                                   }
                                 }
                               });
    return builder.build();
  }

  /**
   * Resolves the {@link DslElementSyntax} for the standalone xml element for the given {@link MetadataType}
   *
   * @param type the {@link MetadataType} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the top level element associated to the {@link MetadataType} or
   *         {@link Optional#empty} if the {@code type} is not supported as an standalone element
   */
  public Optional<DslElementSyntax> resolve(MetadataType type) {
    return type instanceof ObjectType ? resolvePojoDsl(type) : Optional.empty();
  }

  private Optional<DslElementSyntax> resolvePojoDsl(MetadataType type) {

    boolean requiresWrapper = typeRequiresWrapperElement(type);
    boolean supportsInlineDeclaration = supportsInlineDeclaration(type, NOT_SUPPORTED);
    boolean supportTopLevelElement = supportTopLevelElement(type, ElementDslModel.getDefaultInstance());

    if (!supportsInlineDeclaration && !supportTopLevelElement && !requiresWrapper) {
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
        .asWrappedElement(requiresWrapper);

    String typeId = getId(type);
    if (!typeResolvingStack.contains(typeId)) {
      if (supportTopLevelElement || supportsInlineDeclaration) {
        typeResolvingStack.push(typeId);
        declareFieldsAsChilds(builder, ((ObjectType) type).getFields(), namespace, namespaceUri);
        typeResolvingStack.pop();
      }

      DslElementSyntax dsl = builder.build();
      resolvedTypes.put(key, dsl);

      return Optional.of(dsl);
    }

    return Optional.of(builder.build());
  }

  private MetadataTypeVisitor getArrayItemTypeVisitor(final DslElementSyntaxBuilder listBuilder, final String parameterName,
                                                      final String namespace, final String namespaceUri, boolean asItem) {
    return new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {

        if (typeRequiresWrapperElement(objectType)) {
          listBuilder.withGeneric(objectType,
                                  DslElementSyntaxBuilder.create()
                                      .withNamespace(getNamespace(objectType), getNamespaceUri(objectType))
                                      .withElementName(getTopLevelTypeName(objectType))
                                      .supportsChildDeclaration(supportsInlineDeclaration(objectType, NOT_SUPPORTED))
                                      .asWrappedElement(true)
                                      .supportsTopLevelDeclaration(supportTopLevelElement(objectType,
                                                                                          ElementDslModel.getDefaultInstance()))
                                      .build());
        } else if (isValidBean(objectType)) {
          listBuilder.withGeneric(objectType, resolve(objectType).get());
        }
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        DslElementSyntaxBuilder genericBuilder = DslElementSyntaxBuilder.create()
            .withNamespace(namespace, namespaceUri)
            .withElementName(resolveItemName(parameterName, asItem));

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
                                    .build());
      }
    };
  }

  private MetadataTypeVisitor getDictionaryValueTypeVisitor(final DslElementSyntaxBuilder mapBuilder, final String parameterName,
                                                            final String namespace, final String namespaceUri,
                                                            ElementDslModel dslModel) {
    return new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
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
            .withAttributeName(VALUE_ATTRIBUTE)
            .withNamespace(namespace, namespaceUri)
            .withElementName(hyphenize(singularize(parameterName)));
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
                                                    final ObjectFieldType field,
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
        if (isText(field)) {
          objectFieldBuilder.supportsChildDeclaration(true);
          objectFieldBuilder.withElementName(hyphenize(fieldName));
        } else {
          defaultVisit(stringType);
        }
      }

      @Override
      public void visitObject(ObjectType objectType) {

        objectFieldBuilder.withAttributeName(fieldName)
            .withElementName(hyphenize(fieldName))
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

      @Override
      public void visitDictionary(DictionaryType dictionaryType) {
        objectFieldBuilder.withAttributeName(fieldName)
            .withElementName(hyphenize(pluralize(fieldName)))
            .withNamespace(ownerNamespace, ownerNamespaceUri);

        MetadataType keyType = dictionaryType.getKeyType();
        if (supportsInlineDeclaration(keyType, SUPPORTED)) {
          objectFieldBuilder.supportsChildDeclaration(true);

          objectFieldBuilder.withGeneric(dictionaryType.getKeyType(),
                                         DslElementSyntaxBuilder.create().withAttributeName(KEY_ATTRIBUTE).build());

          dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(objectFieldBuilder, fieldName,
                                                                             ownerNamespace, ownerNamespaceUri,
                                                                             ElementDslModel.getDefaultInstance()));
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
                       if (TypeUtils.isContent(field)) {
                         fieldBuilder.supportsChildDeclaration(true);
                         fieldBuilder.withElementName(hyphenize(childName));
                       } else {
                         fieldValue.accept(getObjectFieldVisitor(fieldBuilder, field, childName, namespace, namespaceUri));
                       }

                       objectBuilder.withChild(childName, fieldBuilder.build());
                     }
                   });
  }

  private boolean typeRequiresWrapperElement(MetadataType metadataType) {
    boolean isPojo = metadataType instanceof ObjectType;
    boolean hasSubtypes = subTypesMap.containsBaseType(metadataType);

    return isPojo && (isExtensible(metadataType) || hasSubtypes);
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
}
