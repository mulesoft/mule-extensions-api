/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.createXmlModelProperty;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.getHintsModelProperty;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.Named;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.api.property.XmlHintsModelProperty;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;
import org.mule.runtime.extension.xml.dsl.internal.DslElementSyntaxBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides the {@link DslElementSyntax} of any {@link Named Component},
 * {@link ParameterModel Parameter} or {@link MetadataType Type} within the context
 * of the {@link ExtensionModel Extension model} where the Component was declared.
 *
 * @since 1.0
 */
public class DslSyntaxResolver
{

    private final SubTypesMappingContainer subTypesMapping;
    private final XmlModelProperty extensionXml;
    private final Map<String, DslElementSyntaxBuilder> resolvedTypes = new HashMap<>();
    private Map<MetadataType, XmlModelProperty> importedTypes;

    /**
     * @param model the {@link ExtensionModel} that provides context for resolving the
     *              component's {@link DslElementSyntax}
     * @throws IllegalArgumentException if the {@link ExtensionModel} doesn't have an {@link XmlModelProperty}
     */
    public DslSyntaxResolver(ExtensionModel model)
    {
        this.extensionXml = loadXmlProperties(model);
        this.subTypesMapping = loadSubTypes(model);
        this.importedTypes = loadImportedTypes(model);
    }

    /**
     * Resolves the {@link DslElementSyntax} for the given {@link Named component}.
     *
     * @param component the {@link Named} element to be described in the {@link DslElementSyntax}
     * @return the {@link DslElementSyntax} for the {@link Named model}
     */
    public DslElementSyntax resolve(final Named component)
    {
        return DslElementSyntaxBuilder.create()
                .withElementName(hyphenize(component.getName()))
                .withNamespace(extensionXml.getNamespace(), extensionXml.getNamespaceUri())
                .build();
    }

    /**
     * Resolves the {@link DslElementSyntax} for the given {@link ParameterModel parameter}, providing
     * all the required information for representing this {@code parameter} element in the DSL.
     *
     * @param parameter the {@link ParameterModel} to be described in the {@link DslElementSyntax}
     * @return the {@link DslElementSyntax} for the {@link ParameterModel parameter}
     */
    public DslElementSyntax resolve(final ParameterModel parameter)
    {
        final ExpressionSupport expressionSupport = parameter.getExpressionSupport();
        final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
        final String namespace = getNamespace(parameter.getType());
        final String namespaceUri = getNamespaceUri(parameter.getType());
        final Optional<XmlHintsModelProperty> xmlHints = getHintsModelProperty(parameter);

        parameter.getType().accept(
                new MetadataTypeVisitor()
                {
                    @Override
                    public void visitUnion(UnionType unionType)
                    {
                        unionType.getTypes().forEach(type -> type.accept(this));
                    }

                    @Override
                    protected void defaultVisit(MetadataType metadataType)
                    {
                        builder.withAttributeName(parameter.getName())
                                .withNamespace(namespace, namespaceUri)
                                .withElementName(hyphenize(parameter.getName()));
                    }

                    @Override
                    public void visitArrayType(ArrayType arrayType)
                    {
                        defaultVisit(arrayType);
                        //TODO MULE-9686 review convention of singular/plural
                        MetadataType genericType = arrayType.getType();
                        if (shouldGenerateChildElements(genericType, expressionSupport, xmlHints))
                        {
                            builder.supportsChildDeclaration(true);
                            genericType.accept(getArrayItemTypeVisitor(builder, parameter.getName(), namespace, namespaceUri, false));
                        }
                    }

                    @Override
                    public void visitObject(ObjectType objectType)
                    {
                        builder.withAttributeName(parameter.getName())
                                .withNamespace(namespace, namespaceUri)
                                .withElementName(hyphenize(parameter.getName()))
                                .supportsTopLevelDeclaration(supportTopLevelElement(objectType));

                        if (shouldGenerateChildElements(objectType, expressionSupport, xmlHints))
                        {
                            builder.supportsChildDeclaration(true);

                            if (typeRequiresWrapperElement(objectType))
                            {
                                builder.asWrappedElement(true)
                                        .withNamespace(namespace, namespaceUri);
                            }
                            else
                            {
                                builder.withNamespace(extensionXml.getNamespace(), extensionXml.getNamespaceUri());
                            }
                        }
                    }

                    @Override
                    public void visitDictionary(DictionaryType dictionaryType)
                    {
                        builder.withAttributeName(parameter.getName())
                                .withNamespace(namespace, namespaceUri)
                                .withElementName(hyphenize(pluralize(parameter.getName())))
                                .supportsChildDeclaration(shouldGenerateChildElements(dictionaryType.getKeyType(), expressionSupport));

                        dictionaryType.getValueType().accept(
                                getDictionaryValueTypeVisitor(builder, parameter.getName(), namespace, namespaceUri, xmlHints));
                    }
                }
        );
        return builder.build();
    }

    /**
     * Resolves the xml top level element {@link DslElementSyntax} for the given {@link MetadataType}
     *
     * @param type the {@link MetadataType} to be described in the {@link DslElementSyntax}
     * @return the {@link DslElementSyntax} for the top level element associated to the {@link MetadataType}
     */
    public DslElementSyntax resolve(MetadataType type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Expected a type to resolve its DslSyntax, but type was null");
        }
        final DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create();
        final String namespace = getNamespace(type);
        final String namespaceUri = getNamespaceUri(type);

        String key = getKey(type, namespace, namespaceUri);
        if (resolvedTypes.containsKey(key))
        {
            return resolvedTypes.get(key).build();
        }
        resolvedTypes.put(key, builder);

        type.accept(new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                boolean requiresWrapper = typeRequiresWrapperElement(objectType);
                boolean supportsChildDeclaration = shouldGenerateChildElements(objectType, NOT_SUPPORTED);

                builder.withNamespace(namespace, namespaceUri)
                        .withElementName(getTopLevelTypeName(objectType))
                        .supportsTopLevelDeclaration(supportTopLevelElement(objectType))
                        .supportsChildDeclaration(requiresWrapper || supportsChildDeclaration)
                        .asWrappedElement(requiresWrapper);

                if (supportsChildDeclaration)
                {
                    declareFieldsAsChilds(builder, objectType.getFields(), namespace, namespaceUri);
                }
            }
        });

        return builder.build();
    }

    private String getKey(MetadataType type, String namespace, String namespaceUri)
    {
        return getTypeId(type).orElse(getSingleAnnotation(type, ClassInformationAnnotation.class).map(ClassInformationAnnotation::getName).orElse("")) + namespace + namespaceUri;
    }

    private MetadataTypeVisitor getArrayItemTypeVisitor(final DslElementSyntaxBuilder listBuilder, final String parameterName,
                                                        final String namespace, final String namespaceUri, boolean asItem)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {

                if (typeRequiresWrapperElement(objectType))
                {
                    listBuilder.withGeneric(objectType,
                                            DslElementSyntaxBuilder.create()
                                                    .withNamespace(getNamespace(objectType), getNamespaceUri(objectType))
                                                    .withElementName(getTopLevelTypeName(objectType))
                                                    .supportsChildDeclaration(true)
                                                    .asWrappedElement(true)
                                                    .supportsTopLevelDeclaration(supportTopLevelElement(objectType))
                                                    .build());
                }
                else if (isInstantiable(objectType) && !objectType.getFields().isEmpty())
                {
                    listBuilder.withGeneric(objectType, resolve(objectType));
                }
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementSyntaxBuilder genericBuilder = DslElementSyntaxBuilder.create()
                        .withNamespace(namespace, namespaceUri)
                        .withElementName(getItemName());

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    genericBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(genericBuilder, parameterName, namespace, namespaceUri, true));
                }

                listBuilder.withGeneric(arrayType, genericBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                //TODO MULE-9686 review convention of singular/plural
                listBuilder.withGeneric(metadataType,
                                        DslElementSyntaxBuilder.create()
                                                .withNamespace(namespace, namespaceUri)
                                                .withElementName(getItemName())
                                                .build());
            }

            private String getItemName()
            {
                //TODO MULE-9686 review "item" convention for List<List<?>>
                return asItem ? itemize(singularize(parameterName))
                              : hyphenize(singularize(parameterName));
            }
        };
    }

    private MetadataTypeVisitor getDictionaryValueTypeVisitor(final DslElementSyntaxBuilder mapBuilder, final String parameterName,
                                                              final String namespace, final String namespaceUri, Optional<XmlHintsModelProperty> xmlHints)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                if (shouldGenerateChildElements(objectType, SUPPORTED, xmlHints) ||
                    typeRequiresWrapperElement(objectType))
                {

                    DslElementSyntaxBuilder builder = DslElementSyntaxBuilder.create().withNamespace(namespace, namespaceUri)
                            .withElementName(hyphenize(singularize(parameterName)));

                    addBeanDeclarationSupport(objectType, objectType.getFields(), builder, namespace, namespaceUri);

                    mapBuilder.withGeneric(objectType, builder.build());
                }
                else
                {
                    defaultVisit(objectType);
                }
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementSyntaxBuilder listBuilder = DslElementSyntaxBuilder.create()
                        .withNamespace(namespace, namespaceUri)
                        .withElementName(hyphenize(singularize(parameterName)));

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED, xmlHints))
                {
                    listBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(listBuilder, parameterName, namespace, namespaceUri, true));
                }

                mapBuilder.withGeneric(arrayType, listBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                mapBuilder.withGeneric(metadataType,
                                       DslElementSyntaxBuilder.create()
                                               .withNamespace(namespace, namespaceUri)
                                               .withElementName(hyphenize(singularize(parameterName)))
                                               .build());
            }
        };
    }

    private void addBeanDeclarationSupport(ObjectType objectType, Collection<ObjectFieldType> childFields, DslElementSyntaxBuilder builder, String namespace, String namespaceUri)
    {
        boolean supportsChildDeclaration = shouldGenerateChildElements(objectType, SUPPORTED);
        boolean supportsTopDeclaration = supportTopLevelElement(objectType);

        builder.supportsChildDeclaration(supportsChildDeclaration)
                .supportsTopLevelDeclaration(supportsTopDeclaration);

        if (supportsChildDeclaration || supportsTopDeclaration)
        {
            declareFieldsAsChilds(builder, childFields, namespace, namespaceUri);
        }
    }

    private MetadataTypeVisitor getObjectFieldVisitor(final DslElementSyntaxBuilder objectFieldBuilder, final String fieldName,
                                                      final String ownerNamespace, final String ownerNamespaceUri)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(fieldName))
                        .withNamespace(getNamespace(objectType, ownerNamespace), getNamespaceUri(objectType, ownerNamespaceUri));

                List<ObjectFieldType> fields = objectType.getFields().stream()
                        .filter(f -> !f.getValue().equals(objectType))
                        .collect(toList());

                addBeanDeclarationSupport(objectType, fields, objectFieldBuilder, ownerNamespace, ownerNamespaceUri);
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(fieldName))
                        .withNamespace(ownerNamespace, ownerNamespaceUri);

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    objectFieldBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(objectFieldBuilder, fieldName, ownerNamespace, ownerNamespaceUri, false));
                }
            }

            @Override
            public void visitDictionary(DictionaryType dictionaryType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(pluralize(fieldName)))
                        .withNamespace(ownerNamespace, ownerNamespaceUri);

                MetadataType keyType = dictionaryType.getKeyType();
                if (shouldGenerateChildElements(keyType, SUPPORTED))
                {
                    objectFieldBuilder.supportsChildDeclaration(true);
                    dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(objectFieldBuilder, fieldName,
                                                                                       ownerNamespace, ownerNamespaceUri, empty()));
                }

            }
        };
    }

    private void declareFieldsAsChilds(final DslElementSyntaxBuilder objectBuilder, Collection<ObjectFieldType> fields,
                                       final String namespace, final String namespaceUri)
    {
        fields.forEach(
                field ->
                {
                    DslElementSyntaxBuilder fieldBuilder = DslElementSyntaxBuilder.create();
                    String childName = field.getKey().getName().getLocalPart();
                    field.getValue().accept(getObjectFieldVisitor(fieldBuilder, childName, namespace, namespaceUri));
                    objectBuilder.withChild(childName, fieldBuilder.build());
                });
    }

    private boolean shouldGenerateChildElements(MetadataType metadataType, ExpressionSupport expressionSupport)
    {
        return shouldGenerateChildElements(metadataType, expressionSupport, empty());
    }

    private boolean shouldGenerateChildElements(MetadataType metadataType, ExpressionSupport expressionSupport, Optional<XmlHintsModelProperty> xmlHints)
    {
        final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

        if (REQUIRED == expressionSupport)
        {
            return false;
        }

        metadataType.accept(new MetadataTypeVisitor()
        {
            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                supportsChildDeclaration.set(true);
            }

            @Override
            public void visitAnyType(AnyType anyType)
            {
                supportsChildDeclaration.set(false);
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                arrayType.getType().accept(this);
            }

            @Override
            public void visitObject(ObjectType objectType)
            {
                if (xmlHints.isPresent() && !xmlHints.get().allowsInlineDefinition())
                {
                    supportsChildDeclaration.set(false);
                    return;
                }

                boolean isInstantiable = getSingleAnnotation(metadataType, ClassInformationAnnotation.class)
                        .map(ClassInformationAnnotation::isInstantiable).orElse(false);

                supportsChildDeclaration.set(isExtensible(metadataType) ||
                                             subTypesMapping.containsBaseType(metadataType) ||
                                             (isInstantiable && !((ObjectType) metadataType).getFields().isEmpty()));
            }

            @Override
            public void visitUnion(UnionType unionType)
            {
                supportsChildDeclaration.set(false);
            }

            @Override
            public void visitDictionary(DictionaryType dictionaryType)
            {
                supportsChildDeclaration.set(true);
            }
        });

        return supportsChildDeclaration.get();
    }

    private boolean supportTopLevelElement(MetadataType metadataType)
    {
        final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

        metadataType.accept(new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                boolean isInstantiable = getSingleAnnotation(metadataType, ClassInformationAnnotation.class)
                        .map(ClassInformationAnnotation::isInstantiable).orElse(false);

                supportsChildDeclaration.set(isInstantiable && !((ObjectType) metadataType).getFields().isEmpty());
            }

        });

        return supportsChildDeclaration.get();
    }

    private boolean typeRequiresWrapperElement(MetadataType metadataType)
    {
        boolean isPojo = metadataType instanceof ObjectType;
        boolean hasSubtypes = subTypesMapping.containsBaseType(metadataType);

        return isPojo && (isExtensible(metadataType) || hasSubtypes);
    }

    private String getNamespace(MetadataType type)
    {
        return getNamespace(type, extensionXml.getNamespace());
    }

    private String getNamespace(MetadataType type, String defaultNamespace)
    {
        XmlModelProperty xml = importedTypes.get(type);
        return xml != null ? xml.getNamespace() : defaultNamespace;
    }

    private String getNamespaceUri(MetadataType type)
    {
        return getNamespaceUri(type, extensionXml.getNamespaceUri());
    }

    private String getNamespaceUri(MetadataType type, String defaultUri)
    {
        XmlModelProperty xml = importedTypes.get(type);
        return xml != null ? xml.getNamespaceUri() : defaultUri;
    }

    private boolean isInstantiable(MetadataType metadataType)
    {
        Optional<ClassInformationAnnotation> classInformation = getSingleAnnotation(metadataType, ClassInformationAnnotation.class);
        return classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
    }

    private boolean isExtensible(MetadataType metadataType)
    {
        return getSingleAnnotation(metadataType, ExtensibleTypeAnnotation.class).isPresent();
    }

    private Map<MetadataType, XmlModelProperty> loadImportedTypes(ExtensionModel extension)
    {
        final Map<MetadataType, XmlModelProperty> xmlByType = new HashMap<>();
        extension.getModelProperty(ImportedTypesModelProperty.class)
                .map(ImportedTypesModelProperty::getImportedTypes)
                .ifPresent(imports -> imports
                        .forEach((type, ownerExtension) ->
                                 {
                                     //TODO MULE-10028 stop using `createXmlModelProperty` and `ownerClass`
                                     Class<?> ownerClass = getType(ownerExtension);
                                     xmlByType.put(type, createXmlModelProperty(ownerClass.getAnnotation(Xml.class),
                                                                                ownerClass.getAnnotation(Extension.class).name(), ""));
                                 }));

        return xmlByType;
    }

    private SubTypesMappingContainer loadSubTypes(ExtensionModel extension)
    {
        return new SubTypesMappingContainer(extension.getModelProperty(SubTypesModelProperty.class)
                                                    .map(SubTypesModelProperty::getSubTypesMapping)
                                                    .orElse(Collections.emptyMap()));
    }

    private XmlModelProperty loadXmlProperties(ExtensionModel extension)
    {
        return extension.getModelProperty(XmlModelProperty.class)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("The extension [%s] does not have the [%s], required for its Xml Dsl Resolution",
                                      extension.getName(), XmlModelProperty.class.getSimpleName())));
    }
}
