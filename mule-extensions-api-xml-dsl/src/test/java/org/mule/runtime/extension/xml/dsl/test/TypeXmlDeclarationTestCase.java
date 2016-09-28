/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.test.model.ComplexFieldsType;
import org.mule.runtime.extension.xml.dsl.test.model.GlobalType;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclaration;
import org.mule.runtime.extension.xml.dsl.test.model.NotGlobalType;
import org.mule.runtime.extension.xml.dsl.test.model.RecursiveChainA;
import org.mule.runtime.extension.xml.dsl.test.model.RecursiveChainB;
import org.mule.runtime.extension.xml.dsl.test.model.RecursivePojo;
import org.mule.runtime.extension.xml.dsl.test.model.SimpleFieldsType;

import java.util.Optional;

import org.junit.Test;

public class TypeXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  @Test
  public void textField() {
    MetadataType type = TYPE_LOADER.load(SimpleFieldsType.class);
    DslElementSyntax typeSyntax = getSyntaxResolver().resolve(type)
        .orElseThrow(() -> new RuntimeException("No dsl declaration found for the given type"));
    DslElementSyntax textFieldSyntax = typeSyntax.getChild("textField").get();
    assertThat(textFieldSyntax.getElementName(), is("text-field"));
    assertThat(textFieldSyntax.getAttributeName(), anyOf(is(""), nullValue()));
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
}
