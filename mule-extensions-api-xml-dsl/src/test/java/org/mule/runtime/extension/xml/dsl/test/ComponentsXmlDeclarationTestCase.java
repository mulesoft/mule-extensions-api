/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.runtime.extension.api.model.property.ConfigTypeModelProperty;
import org.mule.runtime.extension.api.model.property.ConnectivityModelProperty;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentsXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  @Test
  public void testOperationDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);

    assertThat(result.getAttributeName(), is(EMPTY));
    assertThat(result.getElementName(), is(hyphenize(OPERATION_NAME)));
    assertThat(result.getNamespace(), is(NAMESPACE));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testSourceDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(source);

    assertThat(result.getAttributeName(), is(EMPTY));
    assertThat(result.getElementName(), is(hyphenize(SOURCE_NAME)));
    assertThat(result.getNamespace(), is(NAMESPACE));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testConfigurationDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(configuration);

    assertThat(result.getAttributeName(), is(EMPTY));
    assertThat(result.getElementName(), is(hyphenize(CONFIGURATION_NAME)));
    assertThat(result.getNamespace(), is(NAMESPACE));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void testConnectionProviderDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(connectionProvider);

    assertThat(result.getAttributeName(), is(EMPTY));
    assertThat(result.getElementName(), is(hyphenize(CONNECTION_PROVIDER_NAME)));
    assertThat(result.getNamespace(), is(NAMESPACE));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(false, result);
    assertIsWrappedElement(false, result);
  }

  @Test
  public void connectedOperation() {
    when(operation.getModelProperty(ConnectivityModelProperty.class)).thenReturn(of(mock(ConnectivityModelProperty.class)));
    DslElementSyntax result = getSyntaxResolver().resolve(operation);

    assertThat(result.requiresConfig(), is(true));
  }

  @Test
  public void operationWithConfig() {
    when(operation.getModelProperty(ConfigTypeModelProperty.class)).thenReturn(of(mock(ConfigTypeModelProperty.class)));
    DslElementSyntax result = getSyntaxResolver().resolve(operation);

    assertThat(result.requiresConfig(), is(true));
  }

}
