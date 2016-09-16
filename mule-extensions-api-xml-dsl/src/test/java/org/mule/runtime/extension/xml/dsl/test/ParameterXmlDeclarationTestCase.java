/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
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
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.api.property.XmlHintsModelProperty;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;
import org.mule.runtime.extension.xml.dsl.test.model.AbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ChildOfAbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ComplexFieldsType;
import org.mule.runtime.extension.xml.dsl.test.model.EmptyType;
import org.mule.runtime.extension.xml.dsl.test.model.ExtensibleType;
import org.mule.runtime.extension.xml.dsl.test.model.GlobalType;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclaration;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclarationWithMapping;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceImplementation;
import org.mule.runtime.extension.xml.dsl.test.model.NonDefaultConstructor;
import org.mule.runtime.extension.xml.dsl.test.model.SimpleFieldsType;
import org.mule.runtime.extension.xml.dsl.test.model.SubType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParameterXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private static final String IMPORT_NAMESPACE = "importns";
  private static final String IMPORT_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/importns";
  private static final String IMPORT_SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/importns/current/mule-importns.xsd";
  private static final String IMPORT_WITH_XML_SCHEMA_LOCATION =
      "http://www.mulesoft.org/schema/mule/importns/current/mule-import-extension-with-xml.xsd";

  private static final String IMPORT_EXTENSION_NAME = "importExtension";
  private static final String IMPORT_EXTENSION_NAME_WITH_XML = "importExtensionWithXml";

  @Test
  public void testSimpleTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testSimplePojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    String fieldName = "sampleString";
    DslElementSyntax childDsl = getChildFieldDsl(fieldName, result);

    assertAttributeName(fieldName, childDsl);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testComplexPojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ComplexFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    assertComplexTypeDslFields(result);
  }

  @Test
  public void xmlStyleAtParameterLevel() {
    XmlHintsModelProperty styleModelProperty = new XmlHintsModelProperty(false, false, false);
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(parameterModel.getModelProperty(XmlHintsModelProperty.class)).thenReturn(of(styleModelProperty));

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertChildElementDeclarationIs(false, result);
  }

  @Test
  public void xmlStyleAtTypeLevel() {
    XmlHintsModelProperty styleModelProperty = new XmlHintsModelProperty(false, false, false);
    when(parameterModel.getType())
        .thenReturn(TYPE_BUILDER.stringType().with(new XmlHintsAnnotation(false, false, false)).build());
    when(parameterModel.getModelProperty(XmlHintsModelProperty.class)).thenReturn(of(styleModelProperty));

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertChildElementDeclarationIs(false, result);
  }

  @Test
  public void testEmptyTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(EmptyType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void topLevelType() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(GlobalType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);
    assertTopElementDeclarationIs(true, result);
  }

  @Test
  public void testExtensibleTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testInterfaceTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclaration.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testAbstractClassParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testNonDefaultConstructorParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(NonDefaultConstructor.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testInterfaceWithMappingParameter() {
    Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
    mapping.put(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                singletonList(TYPE_LOADER.load(InterfaceImplementation.class)));

    when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(of(new SubTypesModelProperty(mapping)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testAbstractWithMappingParameter() {
    Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
    mapping.put(TYPE_LOADER.load(AbstractType.class), singletonList(TYPE_LOADER.load(ChildOfAbstractType.class)));

    when(extension.getModelProperty(SubTypesModelProperty.class))
        .thenReturn(of(new SubTypesModelProperty(mapping)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testImportedFinalTypeWithXmlParameter() {
    Map<MetadataType, String> imports = new HashMap<>();
    imports.put(TYPE_LOADER.load(SimpleFieldsType.class), IMPORT_EXTENSION_NAME_WITH_XML);

    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    when(importOriginMock.getModelProperty(XmlModelProperty.class))
        .thenReturn(of(new XmlModelProperty(EMPTY, IMPORT_NAMESPACE, IMPORT_NAMESPACE_URI, EMPTY, IMPORT_SCHEMA_LOCATION)));

    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));

    when(extension.getModelProperty(ImportedTypesModelProperty.class))
        .thenReturn(of(new ImportedTypesModelProperty(imports)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testImportedFinalTypeWithoutXmlParameter() {
    Map<MetadataType, String> imports = new HashMap<>();
    imports.put(TYPE_LOADER.load(SimpleFieldsType.class), IMPORT_EXTENSION_NAME);

    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    when(importOriginMock.getModelProperty(XmlModelProperty.class))
        .thenReturn(of(new XmlModelProperty(EMPTY, IMPORT_NAMESPACE, IMPORT_NAMESPACE_URI, EMPTY, IMPORT_SCHEMA_LOCATION)));

    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(importOriginMock));

    when(extension.getModelProperty(ImportedTypesModelProperty.class))
        .thenReturn(of(new ImportedTypesModelProperty(imports)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testExtensibleImportParameter() {
    Map<MetadataType, String> imports = new HashMap<>();
    imports.put(TYPE_LOADER.load(ExtensibleType.class), IMPORT_EXTENSION_NAME_WITH_XML);

    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    when(importOriginMock.getModelProperty(XmlModelProperty.class))
        .thenReturn(of(new XmlModelProperty(EMPTY, defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML), IMPORT_NAMESPACE_URI, EMPTY,
                                            IMPORT_WITH_XML_SCHEMA_LOCATION)));

    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));

    when(extension.getModelProperty(ImportedTypesModelProperty.class))
        .thenReturn(of(new ImportedTypesModelProperty(imports)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML), result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testExtensibleImportedTypeWithoutXmlParameter() {
    Map<MetadataType, String> imports = new HashMap<>();
    imports.put(TYPE_LOADER.load(ExtensibleType.class), IMPORT_EXTENSION_NAME);

    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    when(importOriginMock.getModelProperty(XmlModelProperty.class))
        .thenReturn(of(new XmlModelProperty(EMPTY, IMPORT_NAMESPACE, IMPORT_NAMESPACE_URI, EMPTY, IMPORT_SCHEMA_LOCATION)));

    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(importOriginMock));

    when(extension.getModelProperty(ImportedTypesModelProperty.class))
        .thenReturn(of(new ImportedTypesModelProperty(imports)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(IMPORT_NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testMapOfSimpleTypeParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    assertCollectionDslElementSyntax(valueType, parameterModel, PARAMETER_NAME, hyphenize(pluralize(PARAMETER_NAME)),
                                     hyphenize(singularize(PARAMETER_NAME)));
  }

  @Test
  public void testMapOfComplexTypeParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(SimpleFieldsType.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
    assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
    assertChildElementDeclarationIs(true, innerElement);
    assertIsWrappedElement(false, innerElement);
  }

  @Test
  public void testMapOfWrappedTypeParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(ExtensibleType.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
    assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
    assertChildElementDeclarationIs(true, innerElement);
    assertIsWrappedElement(true, innerElement);
  }

  @Test
  public void testMapOfNonInstantiableValueTypeParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(InterfaceDeclaration.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());

    assertCollectionDslElementSyntax(valueType, parameterModel, PARAMETER_NAME, hyphenize(pluralize(PARAMETER_NAME)),
                                     hyphenize(PARAMETER_NAME));
  }

  @Test
  public void testMapOfNonInstantiableValueTypeWithMappedSubtypesParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(InterfaceDeclarationWithMapping.class);

    Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
    mapping.put(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                singletonList(TYPE_LOADER.load(InterfaceImplementation.class)));

    when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(of(new SubTypesModelProperty(mapping)));


    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
    assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
    assertChildElementDeclarationIs(true, innerElement);
    assertIsWrappedElement(true, innerElement);
  }

  @Test
  public void testCollectionOfSimpleTypeParameter() {
    MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    when(parameterModel.getName()).thenReturn(SINGULARIZABLE_PARAMETER_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    assertCollectionDslElementSyntax(itemType, parameterModel, SINGULARIZABLE_PARAMETER_NAME,
                                     hyphenize(SINGULARIZABLE_PARAMETER_NAME),
                                     hyphenize(singularize(SINGULARIZABLE_PARAMETER_NAME)));
  }

  @Test
  public void testCollectionWithSameSingularizedChildName() {
    MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    assertCollectionDslElementSyntax(itemType, parameterModel, PARAMETER_NAME, hyphenize(PARAMETER_NAME),
                                     itemize(PARAMETER_NAME));
  }

  private void assertCollectionDslElementSyntax(MetadataType itemType, ParameterModel parameterModel, String parameterName,
                                                String elementName, String childElementName) {
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(parameterName, result);
    assertElementName(elementName, result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
    assertElementName(childElementName, innerElement);
    assertChildElementDeclarationIs(false, innerElement);
    assertIsWrappedElement(false, innerElement);
  }

  @Test
  public void testCollectionOfComplexTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(SimpleFieldsType.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
    assertElementName(getTopLevelTypeName(itemType), innerElement);
    assertElementNamespace(NAMESPACE, innerElement);
    assertChildElementDeclarationIs(true, innerElement);
    assertIsWrappedElement(false, innerElement);
  }

  @Test
  public void testCollectionOfWrappedTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, result);
    assertElementName(getTopLevelTypeName(itemType), listItemDsl);
    assertElementNamespace(NAMESPACE, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);
  }

  @Test
  public void testCollectionOfNonInstantiableTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(InterfaceDeclaration.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);

    assertThat(result.getGeneric(itemType).isPresent(), is(false));
  }

  @Test
  public void testMapOfListOfComplexTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    DslElementSyntax mapDsl = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, mapDsl);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
    assertElementNamespace(NAMESPACE, mapDsl);
    assertChildElementDeclarationIs(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
    assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, listDsl);
    assertElementName(getTopLevelTypeName(itemType), listItemDsl);
    assertElementNamespace(NAMESPACE, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);
  }

  @Test
  public void testMapOfListOfSimpleTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(String.class);
    MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    DslElementSyntax mapDsl = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, mapDsl);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
    assertElementNamespace(NAMESPACE, mapDsl);
    assertChildElementDeclarationIs(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
    assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    DslElementSyntax itemDsl = getGenericTypeDsl(itemType, listDsl);
    assertElementName(itemize(PARAMETER_NAME), itemDsl);
    assertChildElementDeclarationIs(false, itemDsl);
    assertIsWrappedElement(false, itemDsl);
  }

  @Test
  public void testComplexRecursiveType() {
    MetadataType type = TYPE_LOADER.load(ComplexFieldsType.class);
    DslElementSyntax topDsl = getSyntaxResolver().resolve(type);

    assertElementName(getTopLevelTypeName(type), topDsl);
    assertElementNamespace(NAMESPACE, topDsl);
    assertChildElementDeclarationIs(true, topDsl);
    assertIsWrappedElement(false, topDsl);

    assertComplexTypeDslFields(topDsl);
  }

  @Test
  public void testSubstitutionGroupsFromType() {
    final MetadataType subType = TYPE_LOADER.load(SubType.class);
    final DslElementSyntax subTypeElement = getSyntaxResolver().resolve(subType);
    final List<QName> substitutionGroups = subTypeElement.getSubstitutionGroups();

    assertThat(substitutionGroups, hasSize(2));
    assertThat(substitutionGroups, hasItem(new QName(NAMESPACE_URI, "abstract-sub-type", NAMESPACE)));
    assertThat(substitutionGroups, hasItem(new QName(NAMESPACE_URI, "abstract-super-type", NAMESPACE)));
  }

  private void assertComplexTypeDslFields(DslElementSyntax topDsl) {
    String extensibleTypeListName = "extensibleTypeList";
    DslElementSyntax listDsl = getChildFieldDsl(extensibleTypeListName, topDsl);
    assertAttributeName(extensibleTypeListName, listDsl);
    assertElementName(hyphenize(extensibleTypeListName), listDsl);
    assertElementNamespace(NAMESPACE, listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    MetadataType listItemType = TYPE_LOADER.load(ExtensibleType.class);
    DslElementSyntax listItemDsl = getGenericTypeDsl(listItemType, listDsl);
    assertElementName(getTopLevelTypeName(listItemType), listItemDsl);
    assertElementNamespace(NAMESPACE, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);

    String recursiveChildName = "recursiveChild";
    DslElementSyntax recursiveChildDsl = getChildFieldDsl(recursiveChildName, topDsl);
    assertAttributeName(recursiveChildName, recursiveChildDsl);
    assertElementName(hyphenize(recursiveChildName), recursiveChildDsl);
    assertElementNamespace(NAMESPACE, recursiveChildDsl);
    assertChildElementDeclarationIs(true, recursiveChildDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveChildDsl);
    assertIsWrappedElement(false, recursiveChildDsl);

    String simplePojoName = "simplePojo";
    DslElementSyntax simplePojoDsl = getChildFieldDsl(simplePojoName, topDsl);
    assertAttributeName(simplePojoName, simplePojoDsl);
    assertElementName(hyphenize(simplePojoName), simplePojoDsl);
    assertElementNamespace(NAMESPACE, simplePojoDsl);
    assertChildElementDeclarationIs(true, simplePojoDsl);
    assertIsWrappedElement(false, simplePojoDsl);
    assertTopElementDeclarationIs(false, simplePojoDsl);

    String notGlobalName = "notGlobalType";
    DslElementSyntax notGlobalDsl = getChildFieldDsl(notGlobalName, topDsl);
    assertAttributeName(notGlobalName, notGlobalDsl);
    assertElementName(hyphenize(notGlobalName), notGlobalDsl);
    assertElementNamespace(NAMESPACE, notGlobalDsl);
    assertChildElementDeclarationIs(true, notGlobalDsl);
    assertIsWrappedElement(false, notGlobalDsl);

    String groupedField = "groupedField";
    DslElementSyntax groupedFieldDsl = getChildFieldDsl(groupedField, topDsl);
    assertAttributeName(groupedField, groupedFieldDsl);
    assertElementName("", groupedFieldDsl);
    assertElementNamespace("", groupedFieldDsl);
    assertChildElementDeclarationIs(false, groupedFieldDsl);
    assertIsWrappedElement(false, groupedFieldDsl);

    String anotherGroupedField = "anotherGroupedField";
    DslElementSyntax anotherGroupedFieldDsl = getChildFieldDsl(anotherGroupedField, topDsl);
    assertAttributeName(anotherGroupedField, anotherGroupedFieldDsl);
    assertElementName("", anotherGroupedFieldDsl);
    assertElementNamespace("", anotherGroupedFieldDsl);
    assertChildElementDeclarationIs(false, anotherGroupedFieldDsl);
    assertIsWrappedElement(false, anotherGroupedFieldDsl);

    String parameterGroupType = "parameterGroupType";
    assertThat(topDsl.getChild(parameterGroupType).isPresent(), is(false));
  }

  private DslElementSyntax getGenericTypeDsl(MetadataType itemType, DslElementSyntax result) {
    Optional<DslElementSyntax> genericDsl = result.getGeneric(itemType);
    assertThat("No generic element found for type [" + getTypeId(itemType).orElse("") + "] for element ["
        + result.getElementName() + "]",
               genericDsl.isPresent(), is(true));

    return genericDsl.get();
  }

  private DslElementSyntax getChildFieldDsl(String name, DslElementSyntax parent) {
    Optional<DslElementSyntax> childDsl = parent.getChild(name);
    assertThat("No child element found with name [" + name + "] for element [" + parent.getElementName() + "]",
               childDsl.isPresent(), is(true));

    return childDsl.get();
  }

  @Xml(namespace = IMPORT_NAMESPACE, namespaceLocation = IMPORT_NAMESPACE_URI)
  @Extension(name = IMPORT_EXTENSION_NAME_WITH_XML)
  private static final class ExtensionForImportsDeclaresXml {

  }

  @Extension(name = IMPORT_EXTENSION_NAME)
  private static final class ExtensionForImportsNoXml {

  }
}
