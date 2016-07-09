/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.capability.Xml;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.resolver.DslElementResolver;
import org.mule.runtime.extension.xml.dsl.test.model.AbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ChildOfAbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ComplexFieldsType;
import org.mule.runtime.extension.xml.dsl.test.model.EmptyType;
import org.mule.runtime.extension.xml.dsl.test.model.ExtensibleType;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclaration;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclarationWithMapping;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceImplementation;
import org.mule.runtime.extension.xml.dsl.test.model.NonDefaultConstructor;
import org.mule.runtime.extension.xml.dsl.test.model.SimpleFieldsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParameterXmlDeclarationTestCase extends BaseXmlDeclarationTestCase
{

    @Test
    public void testSimpleTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testSimplePojoParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testComplexPojoParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ComplexFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testEmptyTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(EmptyType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testExtensibleTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testInterfaceTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclaration.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testAbstractClassParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testNonDefaultConstructorParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(NonDefaultConstructor.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testInterfaceWithMappingParameter()
    {
        Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
        mapping.put(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                    singletonList(TYPE_LOADER.load(InterfaceImplementation.class)));

        when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.of(new SubTypesModelProperty(mapping)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testAbstractWithMappingParameter()
    {
        Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
        mapping.put(TYPE_LOADER.load(AbstractType.class), singletonList(TYPE_LOADER.load(ChildOfAbstractType.class)));

        when(extension.getModelProperty(SubTypesModelProperty.class))
                .thenReturn(Optional.of(new SubTypesModelProperty(mapping)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testImportedFinalTypeWithXmlParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(SimpleFieldsType.class), TYPE_LOADER.load(ExtensionForImportsDeclaresXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class))
                .thenReturn(Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testImportedFinalTypeWithoutXmlParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(SimpleFieldsType.class), TYPE_LOADER.load(ExtensionForImportsNoXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class))
                .thenReturn(Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testExtensibleImportParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(ExtensibleType.class), TYPE_LOADER.load(ExtensionForImportsDeclaresXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class))
                .thenReturn(Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertElementNamespace(IMPORT_NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testExtensibleImportedTypeWithoutXmlParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(ExtensibleType.class), TYPE_LOADER.load(ExtensionForImportsNoXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class))
                .thenReturn(Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertElementNamespace(defaultNamespace(IMPORT_EXTENSION_NAME), result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testMapOfSimpleTypeParameter()
    {
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
        MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(valueType, result);
        assertElementName(hyphenize(PARAMETER_NAME), innerElement);
        assertChildElementDeclarationIs(false, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testMapOfComplexTypeParameter()
    {
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
        MetadataType valueType = TYPE_LOADER.load(SimpleFieldsType.class);

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(valueType, result);
        assertElementName(hyphenize(PARAMETER_NAME), innerElement);
        assertChildElementDeclarationIs(true, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testMapOfWrappedTypeParameter()
    {
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
        MetadataType valueType = TYPE_LOADER.load(ExtensibleType.class);

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(valueType, result);
        assertElementName(hyphenize(PARAMETER_NAME), innerElement);
        assertChildElementDeclarationIs(true, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testMapOfNonInstantiableValueTypeParameter()
    {
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
        MetadataType valueType = TYPE_LOADER.load(InterfaceDeclaration.class);

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());

        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(valueType, result);
        assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
        assertChildElementDeclarationIs(false, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testCollectionOfSimpleTypeParameter()
    {
        MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
                                                          .of(itemType)
                                                          .build());

        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(itemType, result);
        assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
        assertChildElementDeclarationIs(false, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testCollectionOfComplexTypeParameter()
    {
        MetadataType itemType = TYPE_LOADER.load(SimpleFieldsType.class);

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
                                                          .of(itemType)
                                                          .build());

        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(itemType, result);
        assertElementName(getTopLevelTypeName(itemType), innerElement);
        assertElementNamespace(NAMESPACE, innerElement);
        assertChildElementDeclarationIs(true, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testCollectionOfWrappedTypeParameter()
    {
        MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
                                                          .of(itemType)
                                                          .build());

        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        DslElementDeclaration innerElement = getGenericTypeDsl(itemType, result);
        assertElementName(getTopLevelTypeName(itemType), innerElement);
        assertElementNamespace(NAMESPACE, innerElement);
        assertChildElementDeclarationIs(true, innerElement);
        assertIsWrappedElement(false, innerElement);
    }

    @Test
    public void testCollectionOfNonInstantiableTypeParameter()
    {
        MetadataType itemType = TYPE_LOADER.load(InterfaceDeclaration.class);
        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
                                                          .of(itemType)
                                                          .build());
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);

        assertThat(result.getGeneric(itemType).isPresent(), is(false));
    }

    @Test
    public void testMapOfListOfComplexTypeParameter()
    {
        MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
        MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration mapDsl = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, mapDsl);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
        assertElementNamespace(NAMESPACE, mapDsl);
        assertChildElementDeclarationIs(true, mapDsl);
        assertIsWrappedElement(false, mapDsl);

        DslElementDeclaration listDsl = getGenericTypeDsl(valueType, mapDsl);
        assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
        assertChildElementDeclarationIs(true, listDsl);
        assertIsWrappedElement(false, listDsl);

        DslElementDeclaration itemDsl = getGenericTypeDsl(itemType, listDsl);
        assertElementName(getTopLevelTypeName(itemType), itemDsl);
        assertChildElementDeclarationIs(true, itemDsl);
        assertIsWrappedElement(false, itemDsl);
    }

    @Test
    public void testMapOfListOfSimpleTypeParameter()
    {
        MetadataType itemType = TYPE_LOADER.load(String.class);
        MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration mapDsl = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, mapDsl);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
        assertElementNamespace(NAMESPACE, mapDsl);
        assertChildElementDeclarationIs(true, mapDsl);
        assertIsWrappedElement(false, mapDsl);

        DslElementDeclaration listDsl = getGenericTypeDsl(valueType, mapDsl);
        assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
        assertChildElementDeclarationIs(true, listDsl);
        assertIsWrappedElement(false, listDsl);

        DslElementDeclaration itemDsl = getGenericTypeDsl(itemType, listDsl);
        assertElementName(itemize(singularize(PARAMETER_NAME)), itemDsl);
        assertChildElementDeclarationIs(false, itemDsl);
        assertIsWrappedElement(false, itemDsl);
    }

    @Test
    public void testComplexRecursiveType()
    {
        MetadataType type = TYPE_LOADER.load(ComplexFieldsType.class);
        DslElementDeclaration topDsl = new DslElementResolver(extension).resolve(type);

        assertElementName(getTopLevelTypeName(type), topDsl);
        assertElementNamespace(NAMESPACE, topDsl);
        assertChildElementDeclarationIs(true, topDsl);
        assertIsWrappedElement(false, topDsl);

        String extensibleTypeListName = "extensibleTypeList";
        DslElementDeclaration listDsl = getChildFieldDsl("extensibleTypeList", topDsl);
        assertAttributeName(extensibleTypeListName, listDsl);
        assertElementName(hyphenize(extensibleTypeListName), listDsl);
        assertElementNamespace(NAMESPACE, listDsl);
        assertChildElementDeclarationIs(true, listDsl);
        assertIsWrappedElement(false, listDsl);

        MetadataType listItemType = TYPE_LOADER.load(ExtensibleType.class);
        DslElementDeclaration innerElement = getGenericTypeDsl(listItemType, listDsl);
        assertElementName(getTopLevelTypeName(listItemType), innerElement);
        assertChildElementDeclarationIs(true, innerElement);
        assertIsWrappedElement(false, innerElement);

        String recursiveChildName = "recursiveChild";
        DslElementDeclaration recursiveChildDsl = getChildFieldDsl(recursiveChildName, topDsl);
        assertAttributeName(recursiveChildName, recursiveChildDsl);
        assertElementName(hyphenize(recursiveChildName), recursiveChildDsl);
        assertElementNamespace(NAMESPACE, recursiveChildDsl);
        assertChildElementDeclarationIs(true, recursiveChildDsl);
        assertIsWrappedElement(false, recursiveChildDsl);

        String simplePojoName = "simplePojo";
        DslElementDeclaration simplePojoDsl = getChildFieldDsl("simplePojo", topDsl);
        assertAttributeName(simplePojoName, simplePojoDsl);
        assertElementName(hyphenize(simplePojoName), simplePojoDsl);
        assertElementNamespace(NAMESPACE, simplePojoDsl);
        assertChildElementDeclarationIs(true, simplePojoDsl);
        assertIsWrappedElement(false, simplePojoDsl);
    }

    private DslElementDeclaration getGenericTypeDsl(MetadataType itemType, DslElementDeclaration result)
    {
        Optional<DslElementDeclaration> genericDsl = result.getGeneric(itemType);
        assertThat("No generic element found for type [" + getTypeId(itemType).orElse("") + "] for element [" + result.getElementName() + "]",
                   genericDsl.isPresent(), is(true));

        return genericDsl.get();
    }

    private DslElementDeclaration getChildFieldDsl(String name, DslElementDeclaration parent)
    {
        Optional<DslElementDeclaration> childDsl = parent.getChild(name);
        assertThat("No child element found with name [" + name + "] for element [" + parent.getElementName() + "]",
                   childDsl.isPresent(), is(true));

        return childDsl.get();
    }

    @Xml(namespace = IMPORT_NAMESPACE, namespaceLocation = IMPORT_NAMESPACE_URI)
    @Extension(name = IMPORT_EXTENSION_NAME)
    private static final class ExtensionForImportsDeclaresXml
    {

    }

    @Extension(name = IMPORT_EXTENSION_NAME)
    private static final class ExtensionForImportsNoXml
    {

    }
}
