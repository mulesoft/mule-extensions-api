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
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.test.model.SimpleFieldsType;

import org.junit.Test;

public class TypeXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  @Test
  public void textField() {
    MetadataType type = TYPE_LOADER.load(SimpleFieldsType.class);
    DslElementSyntax typeSyntax = getSyntaxResolver().resolve(type);
    DslElementSyntax textFieldSyntax = typeSyntax.getChild("textField").get();
    assertThat(textFieldSyntax.getElementName(), is("text-field"));
    assertThat(textFieldSyntax.getAttributeName(), anyOf(is(""), nullValue()));
  }
}
