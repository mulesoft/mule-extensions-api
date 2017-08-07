/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.internal.dsl.DslConstants.KEY_ATTRIBUTE_NAME;
import static org.mule.runtime.internal.dsl.DslConstants.VALUE_ATTRIBUTE_NAME;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.itemize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.dsl.model.AbstractType;
import org.mule.runtime.extension.api.dsl.model.ChildOfAbstractType;
import org.mule.runtime.extension.api.dsl.model.ComplexFieldsType;
import org.mule.runtime.extension.api.dsl.model.ExtensibleType;
import org.mule.runtime.extension.api.dsl.model.GlobalType;
import org.mule.runtime.extension.api.dsl.model.InterfaceDeclaration;
import org.mule.runtime.extension.api.dsl.model.InterfaceDeclarationWithMapping;
import org.mule.runtime.extension.api.dsl.model.InterfaceImplementation;
import org.mule.runtime.extension.api.dsl.model.NonDefaultConstructor;
import org.mule.runtime.extension.api.dsl.model.SimpleFieldsType;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParameterXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  public ParameterXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Test
  public void testStringTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    ifNotContentParameter(() -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertTopLevelDeclarationSupportIs(false, result);
      assertChildElementDeclarationIs(false, result);
    });

    assertChildElementDeclarationIs(isContent(parameterModel), result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testSimpleTypeParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(Integer.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    ifNotContentParameter(() -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertTopLevelDeclarationSupportIs(false, result);
      assertChildElementDeclarationIs(false, result);
    });

    assertChildElementDeclarationIs(isContent(parameterModel), result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testSimplePojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    String fieldName = "sampleString";

    ifContentParameter(() -> assertThat(result.getChild(fieldName).isPresent(), is(false)),
                       () -> {
                         DslElementSyntax childDsl = getAttributeDsl(fieldName, result);
                         assertAttributeName(fieldName, childDsl);
                         assertParameterChildElementDeclaration(true, result);
                         assertIsWrappedElement(false, result);
                       });
  }

  @Test
  public void testComplexPojoParameter() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ComplexFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);


    assertElementName(hyphenize(PARAMETER_NAME), result);
    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertThat(result.getChild(EXTENSIBLE_TYPE_LIST_NAME).isPresent(), is(false)),
                       () -> {
                         assertAttributeName(PARAMETER_NAME, result);
                         assertComplexTypeDslFields(result);
                       });
  }

  @Test
  public void xmlStyleAtParameterLevel() {
    ParameterDslConfiguration dslModel = ParameterDslConfiguration.builder()
        .allowsInlineDefinition(false)
        .allowTopLevelDefinition(false)
        .allowsReferences(false)
        .build();
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(parameterModel.getDslConfiguration()).thenReturn(dslModel);

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(isContent(parameterModel), result);
  }

  @Test
  public void xmlStyleAtTypeLevel() {
    ParameterDslConfiguration dslModel = ParameterDslConfiguration.builder()
        .allowsInlineDefinition(false)
        .allowTopLevelDefinition(false)
        .allowsReferences(false)
        .build();

    when(parameterModel.getType())
        .thenReturn(TYPE_BUILDER.stringType().with(new XmlHintsAnnotation(false, false, false, "", "")).build());
    when(parameterModel.getDslConfiguration()).thenReturn(dslModel);

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertChildElementDeclarationIs(isContent(parameterModel), result);
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
    ObjectType baseType = (ObjectType) TYPE_LOADER.load(InterfaceDeclarationWithMapping.class);
    when(typeCatalog.containsBaseType(baseType)).thenReturn(true);
    when(typeCatalog.getSubTypes(baseType))
        .thenReturn(singleton((ObjectType) TYPE_LOADER.load(InterfaceImplementation.class)));

    when(parameterModel.getType()).thenReturn(baseType);
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testAbstractWithMappingParameter() {
    ObjectType baseType = (ObjectType) TYPE_LOADER.load(AbstractType.class);
    when(typeCatalog.containsBaseType(baseType)).thenReturn(true);
    when(typeCatalog.getSubTypes(baseType))
        .thenReturn(singleton((ObjectType) TYPE_LOADER.load(ChildOfAbstractType.class)));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testImportedFinalTypeWithXmlParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(importOriginMock, IMPORT_EXTENSION_NAME_WITH_XML, SimpleFieldsType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(createImportedXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testImportedFinalTypeWithoutXmlParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(importOriginMock, IMPORT_EXTENSION_NAME, SimpleFieldsType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(createImportedXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testExtensibleImportParameter() {
    ExtensionModel importOriginMock = mock(ExtensionModel.class);
    mockImportedTypes(importOriginMock, IMPORT_EXTENSION_NAME_WITH_XML, ExtensibleType.class);

    when(importOriginMock.getXmlDslModel()).thenReturn(XmlDslModel.builder()
        .setXsdFileName(EMPTY)
        .setPrefix(defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML))
        .setNamespace(IMPORT_NAMESPACE)
        .setXsdFileName(EMPTY)
        .setSchemaLocation(IMPORT_WITH_XML_SCHEMA_LOCATION)
        .build());

    MetadataType paramType = TYPE_LOADER.load(ExtensibleType.class);
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(paramType);

    // When fetching the DSL for the ParameterModel, we should provide the information
    // only related to the parameter, since it's a wrapped imported type
    // That means having the local namespace, and not populating the child fields
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
    assertThat(result.getAttribute("sampleString").isPresent(), is(false));
    assertThat(result.getAttribute("otherNumber").isPresent(), is(false));
    assertThat(result.getAttribute("childNumbers").isPresent(), is(false));
    assertThat(result.getChild("childNumbers").isPresent(), is(false));

  }

  @Test
  public void testExtensibleImportedTypeWithoutXmlParameter() {
    extension = mock(ExtensionModel.class);
    mockImportedTypes(extension, IMPORT_EXTENSION_NAME, ExtensibleType.class);

    when(extension.getXmlDslModel()).thenReturn(createImportedXmlDslModel());
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME)).thenReturn(of(extension));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(IMPORT_PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(true, result);
  }

  @Test
  public void testMapOfSimpleTypeParameter() {
    MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(TYPE_BUILDER.stringType().id(String.class.getName()))
        .build());
    final DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    ifContentParameter(() -> {
      assertNoAttributes(result);
      assertNoGeneric(result, valueType);
    },
                       () -> {
                         assertAttributeName(PARAMETER_NAME, result);
                         assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
                         assertElementNamespace(PREFIX, result);
                         assertParameterChildElementDeclaration(true, result);
                         assertIsWrappedElement(false, result);

                         DslElementSyntax generic = getGenericTypeDsl(valueType, result);
                         assertAttributeDeclaration(false, generic);
                         assertChildElementDeclarationIs(true, generic);
                         assertIsWrappedElement(false, generic);

                         DslElementSyntax key = getAttributeDsl(KEY_ATTRIBUTE_NAME, generic);
                         assertAttributeName(KEY_ATTRIBUTE_NAME, key);
                         assertAttributeDeclaration(true, key);
                         assertChildElementDeclarationIs(false, key);
                         assertIsWrappedElement(false, key);

                         DslElementSyntax value = getAttributeDsl(VALUE_ATTRIBUTE_NAME, generic);
                         assertAttributeName(VALUE_ATTRIBUTE_NAME, value);
                         assertAttributeDeclaration(true, value);
                         assertChildElementDeclarationIs(false, value);
                         assertIsWrappedElement(false, value);
                       });
  }

  @Test
  public void testMapOfComplexTypeParameter() {
    MetadataType valueType = TYPE_LOADER.load(SimpleFieldsType.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(TYPE_LOADER.load(SimpleFieldsType.class))
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);


    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> {
      assertEmptyAttributeName(result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertNoGeneric(result, valueType);
      assertAttributeDeclaration(false, result);
    }, () -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
      assertElementNamespace(PREFIX, result);
      assertParameterChildElementDeclaration(true, result);
      assertIsWrappedElement(false, result);

      DslElementSyntax generic = getGenericTypeDsl(valueType, result);
      assertAttributeDeclaration(false, generic);
      assertChildElementDeclarationIs(true, generic);
      assertIsWrappedElement(false, generic);

      DslElementSyntax key = getAttributeDsl(KEY_ATTRIBUTE_NAME, generic);
      assertAttributeName(KEY_ATTRIBUTE_NAME, key);
      assertAttributeDeclaration(true, key);
      assertChildElementDeclarationIs(false, key);
      assertIsWrappedElement(false, key);

      DslElementSyntax value = getAttributeDsl(VALUE_ATTRIBUTE_NAME, generic);
      assertAttributeName(VALUE_ATTRIBUTE_NAME, value);
      assertAttributeDeclaration(true, value);
      assertChildElementDeclarationIs(true, value);
      assertIsWrappedElement(false, value);
      assertElementName(getTopLevelTypeName(valueType), value);
      getChildFieldDsl("textField", value);
    });
  }

  @Test
  public void testMapOfWrappedTypeParameter() {
    MetadataType valueType = TYPE_LOADER.load(ExtensibleType.class);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(valueType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> {
      assertEmptyAttributeName(result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertNoGeneric(result, valueType);
    }, () -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
      assertElementNamespace(PREFIX, result);
      assertParameterChildElementDeclaration(true, result);
      assertIsWrappedElement(false, result);

      DslElementSyntax generic = getGenericTypeDsl(valueType, result);
      assertAttributeDeclaration(false, generic);
      assertChildElementDeclarationIs(true, generic);
      assertIsWrappedElement(false, generic);

      DslElementSyntax key = getAttributeDsl(KEY_ATTRIBUTE_NAME, generic);
      assertAttributeName(KEY_ATTRIBUTE_NAME, key);
      assertAttributeDeclaration(true, key);
      assertChildElementDeclarationIs(false, key);
      assertIsWrappedElement(false, key);

      DslElementSyntax value = getAttributeDsl(VALUE_ATTRIBUTE_NAME, generic);
      assertAttributeName(VALUE_ATTRIBUTE_NAME, value);
      assertAttributeDeclaration(true, value);
      assertChildElementDeclarationIs(true, value);
      assertIsWrappedElement(false, value);
      assertElementName(getTopLevelTypeName(valueType), value);
    });
  }

  @Test
  public void testMapOfNonInstantiableValueTypeParameter() {
    MetadataType valueType = TYPE_LOADER.load(InterfaceDeclaration.class);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(valueType)
        .with(new ClassInformationAnnotation(Map.class, asList(String.class,
                                                               InterfaceDeclaration.class)))
        .build());

    ifContentParameter(() -> assertNoGeneric(getSyntaxResolver().resolve(parameterModel), valueType),
                       () -> {
                         DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

                         assertAttributeName(PARAMETER_NAME, result);
                         assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
                         assertElementNamespace(PREFIX, result);
                         assertParameterChildElementDeclaration(true, result);
                         assertIsWrappedElement(false, result);

                         DslElementSyntax innerElement = getGenericTypeDsl(valueType, result);
                         assertElementName(hyphenize(singularize(PARAMETER_NAME)), innerElement);
                         assertIsWrappedElement(false, innerElement);
                       });
  }

  @Test
  public void testMapOfNonInstantiableValueTypeWithMappedSubtypesParameter() {
    ObjectType valueType = (ObjectType) TYPE_LOADER.load(InterfaceDeclarationWithMapping.class);

    when(typeCatalog.containsBaseType(valueType)).thenReturn(true);
    when(typeCatalog.getSubTypes(valueType))
        .thenReturn(singleton((ObjectType) TYPE_LOADER.load(InterfaceImplementation.class)));

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(valueType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> {
      assertEmptyAttributeName(result);
      assertElementName(hyphenize(PARAMETER_NAME), result);
      assertNoGeneric(result, valueType);
    }, () -> {
      assertAttributeName(PARAMETER_NAME, result);
      assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
      assertElementNamespace(PREFIX, result);
      assertParameterChildElementDeclaration(true, result);
      assertIsWrappedElement(false, result);

      DslElementSyntax generic = getGenericTypeDsl(valueType, result);
      assertAttributeDeclaration(false, generic);
      assertChildElementDeclarationIs(true, generic);
      assertIsWrappedElement(false, generic);

      DslElementSyntax key = getAttributeDsl(KEY_ATTRIBUTE_NAME, generic);
      assertAttributeName(KEY_ATTRIBUTE_NAME, key);
      assertAttributeDeclaration(true, key);
      assertChildElementDeclarationIs(false, key);
      assertIsWrappedElement(false, key);

      DslElementSyntax value = getAttributeDsl(VALUE_ATTRIBUTE_NAME, generic);
      assertAttributeName(VALUE_ATTRIBUTE_NAME, value);
      assertAttributeDeclaration(true, value);
      assertChildElementDeclarationIs(false, value);
      assertIsWrappedElement(true, value);
      assertElementName("", value);
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
    assertElementNamespace(PREFIX, result);
    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
    assertElementName(childElementName, innerElement);
    assertParameterChildElementDeclaration(true, innerElement);
    assertIsWrappedElement(false, innerElement);
  }

  @Test
  public void testCollectionOfComplexTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(SimpleFieldsType.class);
    when(parameterModel.getName()).thenReturn(COLLECTION_NAME);

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> {
      assertEmptyAttributeName(result);
      assertNoGeneric(result, itemType);
    }, () -> {
      assertAttributeName(COLLECTION_NAME, result);
      assertElementName(hyphenize(COLLECTION_NAME), result);
      assertElementNamespace(PREFIX, result);
      DslElementSyntax innerElement = getGenericTypeDsl(itemType, result);
      assertElementName(getTopLevelTypeName(itemType), innerElement);
      assertElementNamespace(PREFIX, innerElement);
      assertParameterChildElementDeclaration(true, innerElement);
    });

  }

  private void ifNotContentParameter(Runnable test) {
    ifContentParameter(() -> {
    }, test);
  }

  private void assertNoGeneric(DslElementSyntax syntax, MetadataType genericType) {
    assertThat(syntax.getGeneric(genericType).isPresent(), is(false));
  }

  @Test
  public void testCollectionOfWrappedTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    when(parameterModel.getName()).thenReturn(COLLECTION_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());

    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    assertParameterChildElementDeclaration(true, result);
    assertIsWrappedElement(false, result);

    ifContentParameter(() -> assertNoGeneric(result, itemType),
                       () -> {
                         assertElementNamespace(PREFIX, result);
                         assertAttributeName(COLLECTION_NAME, result);
                         assertElementName(hyphenize(COLLECTION_NAME), result);
                         DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, result);
                         assertElementName(getTopLevelTypeName(itemType), listItemDsl);
                         assertElementNamespace(PREFIX, listItemDsl);
                         assertParameterChildElementDeclaration(true, listItemDsl);
                         assertTopElementDeclarationIs(false, listItemDsl);
                         assertIsWrappedElement(true, listItemDsl);
                       });
  }

  @Test
  public void testCollectionOfNonInstantiableTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(InterfaceDeclaration.class);
    when(parameterModel.getName()).thenReturn(COLLECTION_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
        .of(itemType)
        .build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);

    ifNotContentParameter(() -> {
      assertAttributeName(COLLECTION_NAME, result);
      assertElementName(hyphenize(COLLECTION_NAME), result);
      assertElementNamespace(PREFIX, result);
    });
    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);

    assertThat(result.getGeneric(itemType).isPresent(), is(false));
  }

  @Test
  public void testMapOfListOfComplexTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(valueType)
        .build());

    DslElementSyntax mapDsl = getSyntaxResolver().resolve(parameterModel);


    assertElementNamespace(PREFIX, mapDsl);
    assertParameterChildElementDeclaration(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    ifContentParameter(() -> {
      assertEmptyAttributeName(mapDsl);
      assertElementName(hyphenize(PARAMETER_NAME), mapDsl);
      assertNoGeneric(mapDsl, valueType);
    }, () -> {
      assertAttributeName(PARAMETER_NAME, mapDsl);
      assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
      DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
      assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
      assertParameterChildElementDeclaration(true, listDsl);
      assertIsWrappedElement(false, listDsl);

      DslElementSyntax listItemDsl = getGenericTypeDsl(itemType, listDsl);
      assertElementName(getTopLevelTypeName(itemType), listItemDsl);
      assertElementNamespace(PREFIX, listItemDsl);
      assertParameterChildElementDeclaration(true, listItemDsl);
      assertTopElementDeclarationIs(false, listItemDsl);
      assertIsWrappedElement(true, listItemDsl);
    });
  }

  @Test
  public void testMapOfListOfSimpleTypeParameter() {
    MetadataType itemType = TYPE_LOADER.load(String.class);
    MetadataType valueType = TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build();

    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(valueType)
        .build());
    DslElementSyntax mapDsl = getSyntaxResolver().resolve(parameterModel);


    assertElementNamespace(PREFIX, mapDsl);
    assertParameterChildElementDeclaration(true, mapDsl);
    assertIsWrappedElement(false, mapDsl);

    ifContentParameter(() -> {
      assertEmptyAttributeName(mapDsl);
      assertElementName(hyphenize(PARAMETER_NAME), mapDsl);
      assertNoGeneric(mapDsl, valueType);
    }, () -> {
      assertAttributeName(PARAMETER_NAME, mapDsl);
      assertElementName(hyphenize(pluralize(PARAMETER_NAME)), mapDsl);
      DslElementSyntax listDsl = getGenericTypeDsl(valueType, mapDsl);
      assertElementName(hyphenize(singularize(PARAMETER_NAME)), listDsl);
      assertParameterChildElementDeclaration(true, listDsl);
      assertIsWrappedElement(false, listDsl);

      DslElementSyntax itemDsl = getGenericTypeDsl(itemType, listDsl);
      assertElementName(itemize(PARAMETER_NAME), itemDsl);
      assertParameterChildElementDeclaration(true, itemDsl);
      assertIsWrappedElement(false, itemDsl);
    });
  }

  @Test
  public void testListOfMapsParameter() {
    ObjectType dictionary = TYPE_BUILDER.objectType().id(Map.class.getName())
        .openWith(TYPE_BUILDER.stringType().id(String.class.getName()))
        .build();

    when(parameterModel.getName()).thenReturn(COLLECTION_NAME);
    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName()).of(dictionary).build());
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    ifNotContentParameter(() -> {
      assertAttributeName(COLLECTION_NAME, result);
      assertElementName(hyphenize(COLLECTION_NAME), result);
      assertElementNamespace(PREFIX, result);
    });
    assertParameterChildElementDeclaration(false, result);
    assertIsWrappedElement(false, result);
  }

}
