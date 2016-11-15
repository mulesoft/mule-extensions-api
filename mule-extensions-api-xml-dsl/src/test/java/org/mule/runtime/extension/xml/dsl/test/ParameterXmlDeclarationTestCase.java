/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Collections.singleton;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
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

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParameterXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private static final String IMPORT_NAMESPACE = "importns";
  private static final String IMPORT_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/importns";
  private static final String IMPORT_SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/importns/current/mule-importns.xsd";
  private static final String IMPORT_WITH_XML_SCHEMA_LOCATION =
      "http://www.mulesoft.org/schema/mule/importns/current/mule-import-extension-with-xml.xsd";

  private static final String IMPORT_EXTENSION_NAME = "importExtension";
  private static final String IMPORT_EXTENSION_NAME_WITH_XML = "importExtensionWithXml";

  public ParameterXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Test
  public void testSimpleTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    ifNotContentParameter(() -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertElementNamespace(NAMESPACE, result);
    });
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testSimplePojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    String fieldName = "sampleString";

    ifContentParameter(() -> assertThat(result.getChild(fieldName).isPresent(), is(false)),
                       () -> {
                         DslElementSyntax childDsl = getChildFieldDsl(fieldName, result);
                         assertAttributeName(fieldName, childDsl);
                         assertParameterChildElementDeclaration(true, result);
                         assertIsWrappedElement(false, result);
                       });
  }

  @Test
  public void testComplexPojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ComplexFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertThat(result.getChild(EXTENSIBLE_TYPE_LIST_NAME).isPresent(), is(false)),
                       () -> assertComplexTypeDslFields(result));;
  }

  @Test
  public void xmlStyleAtParameterLevel() {
    ElementDslModel dslModel = ElementDslModel.builder()
        .allowsInlineDefinition(false)
        .allowTopLevelDefinition(false)
        .allowsReferences(false)
        .build();
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(parameterModel.getDslModel()).thenReturn(dslModel);

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertChildElementDeclarationIs(false, result);
  }

  @Test
  public void xmlStyleAtTypeLevel() {
    ElementDslModel dslModel = ElementDslModel.builder()
        .allowsInlineDefinition(false)
        .allowTopLevelDefinition(false)
        .allowsReferences(false)
        .build();

    when(parameterModel.getType())
        .thenReturn(TYPE_BUILDER.stringType().with(new XmlHintsAnnotation(false, false, false)).build());
    when(parameterModel.getDslModel()).thenReturn(dslModel);

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertChildElementDeclarationIs(false, result);
  }

  @Test
  public void testEmptyTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(EmptyType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void topLevelType() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(GlobalType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);
    assertTopElementDeclarationIs(true, result);
  }

  @Test
  public void testExtensibleTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testInterfaceTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclaration.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testAbstractClassParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testNonDefaultConstructorParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(NonDefaultConstructor.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testInterfaceWithMappingParameter() {
    when(extension.getSubTypes()).thenReturn(singleton(
                                                       new SubTypesModel(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                                                                         singleton(TYPE_LOADER
                                                                             .load(InterfaceImplementation.class)))));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testAbstractWithMappingParameter() {
    when(extension.getSubTypes()).thenReturn(singleton(
                                                       new SubTypesModel(TYPE_LOADER.load(AbstractType.class),
                                                                         singleton(TYPE_LOADER
                                                                             .load(ChildOfAbstractType.class)))));


    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testImportedFinalTypeWithXmlParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(IMPORT_EXTENSION_NAME_WITH_XML, SimpleFieldsType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(createTestXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testImportedFinalTypeWithoutXmlParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(IMPORT_EXTENSION_NAME, SimpleFieldsType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(createTestXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testExtensibleImportParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(IMPORT_EXTENSION_NAME_WITH_XML, ExtensibleType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(XmlDslModel.builder()
        .setXsdFileName(EMPTY)
        .setNamespace(defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML))
        .setNamespaceUri(IMPORT_NAMESPACE_URI)
        .setXsdFileName(EMPTY)
        .setSchemaLocation(IMPORT_WITH_XML_SCHEMA_LOCATION)
        .build());

    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML), result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testExtensibleImportedTypeWithoutXmlParameter() {
    extension = mock(ExtensionModel.class);
    mockImportedTypes(IMPORT_EXTENSION_NAME, ExtensibleType.class);

    when(extension.getXmlDslModel()).thenReturn(createTestXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(extension));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(IMPORT_NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
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
    ifContentParameter(() -> assertNoGeneric(getSyntaxResolver().resolve(parameterModel), valueType),
                       () -> assertCollectionDslElementSyntax(valueType, parameterModel, PARAMETER_NAME,
                                                              hyphenize(pluralize(PARAMETER_NAME)),
                                                              hyphenize(singularize(PARAMETER_NAME))));
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
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, valueType),
                       () -> {
                         DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
                         assertParameterChildElementDeclaration(true, innerElement);
                         assertIsWrappedElement(false, innerElement);
                       });
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
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, valueType),
                       () -> {
                         DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
                         assertParameterChildElementDeclaration(true, innerElement);
                         assertIsWrappedElement(true, innerElement);
                       });
  }

  @Test
  public void testMapOfNonInstantiableValueTypeParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(InterfaceDeclaration.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());
    ifContentParameter(() -> assertNoGeneric(getSyntaxResolver().resolve(parameterModel), valueType),
                       () -> assertCollectionDslElementSyntax(valueType, parameterModel, PARAMETER_NAME,
                                                              hyphenize(pluralize(PARAMETER_NAME)),
                                                              hyphenize(PARAMETER_NAME)));
  }

  @Test
  public void testMapOfNonInstantiableValueTypeWithMappedSubtypesParameter() {
    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    MetadataType valueType = TYPE_LOADER.load(InterfaceDeclarationWithMapping.class);

    when(extension.getSubTypes()).thenReturn(singleton(new SubTypesModel(
                                                                         TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                                                                         singleton(TYPE_LOADER
                                                                             .load(InterfaceImplementation.class)))));



    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
        .ofKey(keyType)
        .ofValue(valueType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(PARAMETER_NAME, result);
    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    assertElementNamespace(NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, valueType),
                       () -> {
                         DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
                         assertParameterChildElementDeclaration(true, innerElement);
                         assertIsWrappedElement(true, innerElement);
                       });
  }

  @Test
  public void testCollectionOfSimpleTypeParameter() {
    MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    when(parameterModel.getName()).thenReturn(SINGULARIZABLE_PARAMETER_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    ifContentParameter(() -> assertNoGeneric(getSyntaxResolver().resolve(parameterModel), itemType),
                       () -> assertCollectionDslElementSyntax(itemType, parameterModel, SINGULARIZABLE_PARAMETER_NAME,
                                                              hyphenize(SINGULARIZABLE_PARAMETER_NAME),
                                                              hyphenize(singularize(SINGULARIZABLE_PARAMETER_NAME))));
  }

  @Test
  public void testCollectionWithSameSingularizedChildName() {
    MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());
    ifContentParameter(() -> {
      DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
      assertNoGeneric(result, itemType);
    },
                       () -> assertCollectionDslElementSyntax(itemType, parameterModel, PARAMETER_NAME,
                                                              hyphenize(PARAMETER_NAME),
                                                              itemize(PARAMETER_NAME)));
  }

  private void assertCollectionDslElementSyntax(MetadataType itemType, ParameterModel parameterModel, String parameterName,
                                                String elementName, String childElementName) {
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertAttributeName(parameterName, result);
    assertElementName(elementName, result);
    assertElementNamespace(NAMESPACE, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
    assertElementName(childElementName, innerElement);
    assertParameterChildElementDeclaration(false, innerElement);
    assertIsWrappedElement(false, innerElement);
  }

  @Test
  public void testCollectionOfComplexTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(SimpleFieldsType.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, itemType),
                       () -> {
                         assertAttributeName(PARAMETER_NAME, result);
                         assertElementName(hyphenize(PARAMETER_NAME), result);
                         assertElementNamespace(NAMESPACE, result);
                         DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
                         assertElementName(getTopLevelTypeName(itemType), innerElement);
                         assertElementNamespace(NAMESPACE, innerElement);
                         assertParameterChildElementDeclaration(true, innerElement);
                       });
  }

  private void ifNotContentParameter(Runnable test) {
    ifContentParameter(() -> {
    }, test);
  }

  private void ifContentParameter(Runnable test, Runnable orElse) {
    if (isContent(parameterModel)) {
      test.run();
    } else {
      orElse.run();
    }
  }

  private void assertNoGeneric(DslElementSyntax syntax, MetadataType genericType) {
    assertThat(syntax.getGeneric(genericType).isPresent(), is(false));
  }

  @Test
  public void testCollectionOfWrappedTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, itemType),
                       () -> {
                         assertElementNamespace(NAMESPACE, result);
                         assertAttributeName(PARAMETER_NAME, result);
                         assertElementName(hyphenize(PARAMETER_NAME), result);
                         DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, result);
                         assertElementName(getTopLevelTypeName(itemType), listItemDsl);
                         assertElementNamespace(NAMESPACE, listItemDsl);
                         assertParameterChildElementDeclaration(true, listItemDsl);
                         assertTopElementDeclarationIs(false, listItemDsl);
                         assertIsWrappedElement(true, listItemDsl);
                       });
  }

  @Test
  public void testCollectionOfNonInstantiableTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(InterfaceDeclaration.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    ifNotContentParameter(() -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertElementNamespace(NAMESPACE, result);
    });
    assertParameterChildElementDeclaration(false, result);
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
    assertParameterChildElementDeclaration(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    ifContentParameter(() -> assertNoGeneric(mapDsl, valueType),
                       () -> {
                         DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
                         assertParameterChildElementDeclaration(true, listDsl);
                         assertIsWrappedElement(false, listDsl);

                         DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, listDsl);
                         assertElementName(getTopLevelTypeName(itemType), listItemDsl);
                         assertElementNamespace(NAMESPACE, listItemDsl);
                         assertParameterChildElementDeclaration(true, listItemDsl);
                         assertTopElementDeclarationIs(false, listItemDsl);
                         assertIsWrappedElement(true, listItemDsl);
                       });
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
    assertParameterChildElementDeclaration(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    ifContentParameter(() -> assertNoGeneric(mapDsl, valueType),
                       () -> {
                         DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
                         assertParameterChildElementDeclaration(true, listDsl);
                         assertIsWrappedElement(false, listDsl);

                         DslElementSyntax itemDsl = getGenericTypeDsl(itemType, listDsl);
                         assertElementName(itemize(PARAMETER_NAME), itemDsl);
                         assertParameterChildElementDeclaration(false, itemDsl);
                         assertIsWrappedElement(false, itemDsl);
                       });
  }

  private XmlDslModel createTestXmlDslModel() {
    return XmlDslModel.builder()
        .setXsdFileName(EMPTY)
        .setNamespace(IMPORT_NAMESPACE)
        .setNamespaceUri(IMPORT_NAMESPACE_URI)
        .setSchemaLocation(IMPORT_SCHEMA_LOCATION)
        .setSchemaVersion(EMPTY)
        .build();
  }

  private void mockImportedTypes(String originExtension, Class<?> type) {
    when(extension.getImportedTypes()).thenReturn(singleton(new ImportedTypeModel(originExtension, TYPE_LOADER.load(type))));
  }

  @Test
  public void testListOfMapsParameter() {
    DictionaryType dictionary = TYPE_BUILDER.dictionaryType()
        .ofKey(TYPE_BUILDER.stringType())
        .ofValue(TYPE_BUILDER.stringType())
        .build();
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName()).of(dictionary).build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    ifNotContentParameter(() -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertElementNamespace(NAMESPACE, result);
    });
    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

  @Xml(namespace = IMPORT_NAMESPACE, namespaceLocation = IMPORT_NAMESPACE_URI)
  @Extension(name = IMPORT_EXTENSION_NAME_WITH_XML)
  private static final class ExtensionForImportsDeclaresXml {

  }


  @Extension(name = IMPORT_EXTENSION_NAME)
  private static final class ExtensionForImportsNoXml {

  }
}
