/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.util.stream.Collectors.toList;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.createXmlModelProperty;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.capability.Xml;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.Named;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;
import org.mule.runtime.extension.xml.dsl.internal.DslElementDeclarationBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides the {@link DslElementDeclaration} of any {@link Named Component},
 * {@link ParameterModel Parameter} or {@link MetadataType Type} within the context
 * of the {@link ExtensionModel Extension model} where the Component was declared.
 *
 * @since 1.0
 */
public class DslElementResolver
{

    private Map<MetadataType, XmlModelProperty> importedTypes;
    private final SubTypesMappingContainer subTypesMapping;
    private final XmlModelProperty extensionXml;

    /**
     * @param model the {@link ExtensionModel} that provides context for resolving the
     *              component's {@link DslElementDeclaration}
     * @throws IllegalArgumentException if the {@link ExtensionModel} doesn't have an {@link XmlModelProperty}
     */
    public DslElementResolver(ExtensionModel model)
    {
        this.extensionXml = loadXmlProperties(model);
        this.subTypesMapping = loadSubTypes(model);
        this.importedTypes = loadImportedTypes(model);
    }

    /**
     * Resolves the {@link DslElementDeclaration} for the given {@link Named component}.
     *
     * @param component the {@link Named} element to be described in the {@link DslElementDeclaration}
     * @return the {@link DslElementDeclaration} for the {@link Named model}
     */
    public DslElementDeclaration resolve(final Named component)
    {
        return DslElementDeclarationBuilder.create()
                .withElementName(hyphenize(component.getName()))
                .withNamespace(extensionXml.getNamespace())
                .build();
    }

    /**
     * Resolves the {@link DslElementDeclaration} for the given {@link ParameterModel parameter}, providing
     * all the required information for representing this {@code parameter} element in the DSL.
     *
     * @param parameter the {@link ParameterModel} to be described in the {@link DslElementDeclaration}
     * @return the {@link DslElementDeclaration} for the {@link ParameterModel parameter}
     */
    public DslElementDeclaration resolve(final ParameterModel parameter)
    {
        final ExpressionSupport expressionSupport = parameter.getExpressionSupport();
        final DslElementDeclarationBuilder builder = DslElementDeclarationBuilder.create();
        final String namespace = getNamespace(parameter.getType());

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
                                .withNamespace(namespace)
                                .withElementName(hyphenize(parameter.getName()));
                    }

                    @Override
                    public void visitArrayType(ArrayType arrayType)
                    {
                        defaultVisit(arrayType);
                        //TODO MULE-10029 review convention of singular/plural
                        MetadataType genericType = arrayType.getType();
                        if (shouldGenerateChildElements(genericType, expressionSupport))
                        {
                            builder.supportsChildDeclaration(true);
                            genericType.accept(getArrayItemTypeVisitor(builder, parameter.getName(), namespace, false));
                        }
                    }

                    @Override
                    public void visitObject(ObjectType objectType)
                    {
                        builder.withAttributeName(parameter.getName())
                                .withNamespace(namespace)
                                .withElementName(hyphenize(parameter.getName()));

                        if (shouldGenerateChildElements(objectType, expressionSupport))
                        {
                            builder.supportsChildDeclaration(true);

                            if (typeRequiresWrapperElement(objectType))
                            {
                                builder.asWrappedElement(true)
                                        .withNamespace(namespace);
                            }
                            else
                            {
                                builder.withNamespace(extensionXml.getNamespace());
                            }
                        }
                    }

                    @Override
                    public void visitDictionary(DictionaryType dictionaryType)
                    {
                        builder.withAttributeName(parameter.getName())
                                .withNamespace(namespace)
                                .withElementName(hyphenize(pluralize(parameter.getName())))
                                .supportsChildDeclaration(shouldGenerateChildElements(dictionaryType.getKeyType(), expressionSupport));

                        dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(builder, parameter.getName(), namespace));
                    }
                }
        );
        return builder.build();
    }

    /**
     * Resolves the xml top level element {@link DslElementDeclaration} for the given {@link MetadataType}
     *
     * @param type the {@link MetadataType} to be described in the {@link DslElementDeclaration}
     * @return the {@link DslElementDeclaration} for the top level element associated to the {@link MetadataType}
     */
    public DslElementDeclaration resolve(MetadataType type)
    {
        DslElementDeclarationBuilder typeBuilder = DslElementDeclarationBuilder.create();
        resolve(type, typeBuilder);
        return typeBuilder.build();
    }

    private MetadataTypeVisitor getArrayItemTypeVisitor(final DslElementDeclarationBuilder listBuilder, final String parameterName, final String namespace, boolean asItem)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                if (isInstantiable(objectType) && !objectType.getFields().isEmpty() && !typeRequiresWrapperElement(objectType))
                {
                    listBuilder.withGeneric(objectType, resolve(objectType));
                }
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementDeclarationBuilder genericBuilder = DslElementDeclarationBuilder.create()
                        .withNamespace(namespace)
                        .withElementName(getItemName());

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    genericBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(genericBuilder, parameterName, namespace, true));
                }

                listBuilder.withGeneric(arrayType, genericBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                //TODO MULE-10029 review convention of singular/plural
                listBuilder.withGeneric(metadataType,
                                        DslElementDeclarationBuilder.create()
                                                .withNamespace(namespace)
                                                .withElementName(getItemName())
                                                .build());
            }

            private String getItemName()
            {
                //TODO MULE-10029 review "item" convention for List<List<?>>
                return asItem ? itemize(singularize(parameterName))
                              : hyphenize(singularize(parameterName));
            }
        };
    }

    private MetadataTypeVisitor getDictionaryValueTypeVisitor(final DslElementDeclarationBuilder mapBuilder, final String parameterName, final String namespace)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                DslElementDeclarationBuilder valueChildBuilder = DslElementDeclarationBuilder.create()
                        .withNamespace(namespace)
                        //TODO MULE-10029 handle wrapped types for maps xml generation (not required for parsers)
                        .withElementName(hyphenize(singularize(parameterName)))
                        .supportsChildDeclaration(shouldGenerateChildElements(objectType, SUPPORTED));

                mapBuilder.withGeneric(objectType, valueChildBuilder.build());
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementDeclarationBuilder listBuilder = DslElementDeclarationBuilder.create()
                        .withNamespace(namespace)
                        .withElementName(hyphenize(singularize(parameterName)));

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    listBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(listBuilder, parameterName, namespace, true));
                }

                mapBuilder.withGeneric(arrayType, listBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                mapBuilder.withGeneric(metadataType,
                                       DslElementDeclarationBuilder.create()
                                               .withNamespace(namespace)
                                               .withElementName(hyphenize(singularize(parameterName)))
                                               .build());
            }
        };
    }

    private DslElementDeclaration resolve(MetadataType type, final DslElementDeclarationBuilder builder)
    {
        final String namespace = getNamespace(type);

        type.accept(new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                builder.withNamespace(namespace)
                        .withElementName(getTopLevelTypeName(objectType))
                        .supportsChildDeclaration(shouldGenerateChildElements(objectType, SUPPORTED));

                declareFieldsAsChilds(builder, objectType.getFields(), namespace);
            }

        });

        return builder.build();
    }

    private MetadataTypeVisitor getObjectFieldVisitor(final DslElementDeclarationBuilder objectFieldBuilder,
                                                      final String fieldName, final String ownerNamespace)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(fieldName))
                        .withNamespace(getNamespace(objectType, ownerNamespace))
                        .supportsChildDeclaration(shouldGenerateChildElements(objectType, ExpressionSupport.SUPPORTED));

                List<ObjectFieldType> fields = objectType.getFields().stream()
                        .filter(f -> !f.getValue().equals(objectType))
                        .collect(toList());

                declareFieldsAsChilds(objectFieldBuilder, fields, ownerNamespace);
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(fieldName))
                        .withNamespace(ownerNamespace);

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, ExpressionSupport.SUPPORTED))
                {
                    objectFieldBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(objectFieldBuilder, fieldName, ownerNamespace, false));
                }
            }

            @Override
            public void visitDictionary(DictionaryType dictionaryType)
            {
                objectFieldBuilder.withAttributeName(fieldName)
                        .withElementName(hyphenize(pluralize(fieldName)))
                        .withNamespace(ownerNamespace);

                MetadataType keyType = dictionaryType.getKeyType();
                objectFieldBuilder.supportsChildDeclaration(shouldGenerateChildElements(keyType, ExpressionSupport.SUPPORTED));

                dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(objectFieldBuilder, fieldName, ownerNamespace));
            }
        };
    }

    private void declareFieldsAsChilds(final DslElementDeclarationBuilder objectBuilder,
                                       Collection<ObjectFieldType> fields, final String namespace)
    {
        fields.forEach(
                field ->
                {
                    DslElementDeclarationBuilder fieldBuilder = DslElementDeclarationBuilder.create();
                    String childName = field.getKey().getName().getLocalPart();
                    field.getValue().accept(getObjectFieldVisitor(fieldBuilder, childName, namespace));
                    objectBuilder.withChild(childName, fieldBuilder.build());
                });
    }

    private boolean shouldGenerateChildElements(MetadataType metadataType, ExpressionSupport expressionSupport)
    {
        final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

        if (ExpressionSupport.REQUIRED == expressionSupport)
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
            public void visitArrayType(ArrayType arrayType)
            {
                arrayType.getType().accept(this);
            }

            @Override
            public void visitObject(ObjectType objectType)
            {
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

    private boolean typeRequiresWrapperElement(MetadataType metadataType)
    {
        boolean isPojo = metadataType instanceof ObjectType;
        boolean hasSubtypes = subTypesMapping.containsBaseType(metadataType);

        return isPojo && (isExtensible(metadataType) || hasSubtypes);
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

    private String getNamespace(MetadataType type)
    {
        return getNamespace(type, extensionXml.getNamespace());
    }

    private String getNamespace(MetadataType type, String defaultNamespace)
    {
        XmlModelProperty xml = importedTypes.get(type);
        return xml != null ? xml.getNamespace() : defaultNamespace;
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
}
