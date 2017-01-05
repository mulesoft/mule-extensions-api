/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.dsl.model.ComplexFieldsType;
import org.mule.runtime.extension.api.dsl.model.ExtensibleType;
import org.mule.runtime.extension.api.dsl.model.GlobalType;
import org.mule.runtime.extension.api.dsl.model.InterfaceDeclaration;
import org.mule.runtime.extension.api.dsl.model.NotGlobalType;
import org.mule.runtime.extension.api.dsl.model.RecursiveChainA;
import org.mule.runtime.extension.api.dsl.model.RecursiveChainB;
import org.mule.runtime.extension.api.dsl.model.RecursivePojo;
import org.mule.runtime.extension.api.dsl.model.SimpleFieldsType;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TypeXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  public TypeXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Test
  public void textField() {
    MetadataType type = TYPE_LOADER.load(SimpleFieldsType.class);
    DslElementSyntax typeSyntax = getSyntaxResolver().resolve(type)
        .orElseThrow(() -> new RuntimeException("No dsl declaration found for the given type"));
    DslElementSyntax textFieldSyntax = typeSyntax.getChild("textField").get();
    assertThat(textFieldSyntax.getElementName(), is("text-field"));
    assertThat(textFieldSyntax.getAttributeName(), is(""));
  }


  @Test
  public void testRecursiveTypeAndChain() {
    MetadataType type = TYPE_LOADER.load(RecursivePojo.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));

    type = TYPE_LOADER.load(RecursiveChainA.class);
    topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));

    type = TYPE_LOADER.load(RecursiveChainB.class);
    topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));
  }


  @Test
  public void testComplexRecursiveType() {
    MetadataType type = TYPE_LOADER.load(ComplexFieldsType.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));
    assertElementName(getTopLevelTypeName(type), topDsl.get());
    assertElementNamespace(NAMESPACE, topDsl.get());
    assertChildElementDeclarationIs(true, topDsl.get());
    assertIsWrappedElement(false, topDsl.get());

    assertComplexTypeDslFields(topDsl.get());
  }

  @Test
  public void testNoGlobalType() {
    MetadataType type = TYPE_LOADER.load(NotGlobalType.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));
    assertElementName(getTopLevelTypeName(type), topDsl.get());
    assertElementNamespace(NAMESPACE, topDsl.get());
    assertChildElementDeclarationIs(true, topDsl.get());
    assertTopElementDeclarationIs(false, topDsl.get());
    assertIsWrappedElement(false, topDsl.get());
  }

  @Test
  public void testGlobalType() {
    MetadataType type = TYPE_LOADER.load(GlobalType.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration expected but none applied", topDsl.isPresent(), is(true));
    assertElementName(getTopLevelTypeName(type), topDsl.get());
    assertElementNamespace(NAMESPACE, topDsl.get());
    assertChildElementDeclarationIs(true, topDsl.get());
    assertTopElementDeclarationIs(true, topDsl.get());
    assertIsWrappedElement(false, topDsl.get());
  }

  @Test
  public void testAbsentIfNotComplex() {
    MetadataType type = TYPE_LOADER.load(String.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration not expected but one was found", topDsl.isPresent(), is(false));
  }

  @Test
  public void testAbsentIfNotGlobalWrappedNorChild() {
    MetadataType type = TYPE_LOADER.load(InterfaceDeclaration.class);
    Optional<DslElementSyntax> topDsl = getSyntaxResolver().resolve(type);

    assertThat("Type dsl declaration not expected but one was found", topDsl.isPresent(), is(false));
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

    MetadataType paramType = TYPE_LOADER.load(ExtensibleType.class);
    when(dslContext.getExtension(IMPORT_EXTENSION_NAME_WITH_XML)).thenReturn(of(importOriginMock));
    when(parameterModel.getType()).thenReturn(paramType);

    // When fetching the DSL for the MetadataType, we should provide all the information
    // related to the Type representation.
    // That means having the original namespace from the Extension from where this was imported,
    // and populating the child fields and DSL information required for writing the type
    Optional<DslElementSyntax> typeResult = getSyntaxResolver().resolve(paramType);
    assertThat(typeResult.isPresent(), is(true));
    assertElementNamespace(defaultNamespace(IMPORT_EXTENSION_NAME_WITH_XML), typeResult.get());
    assertExtensibleTypeDslStructure(typeResult.get());
  }
}
