/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;
import org.mule.runtime.extension.internal.loader.enricher.NamedObjectDeclarationEnricher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NamedObjectDeclarationEnricherTestCase {

  @Mock
  private ExtensionLoadingContext extensionLoadingContext;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclarer extensionDeclarer;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclaration extensionDeclaration;

  private NamedObjectDeclarationEnricher enricher = new NamedObjectDeclarationEnricher();

  private ConfigurationDeclaration configurationDeclaration;

  @Before
  public void before() {
    configurationDeclaration = spy(new ExtensionDeclarer().withConfig("testConfiguration")).getDeclaration();

    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionDeclaration.getConfigurations()).thenReturn(singletonList(configurationDeclaration));
  }

  @Test
  public void enrichedComponentModelHasNameParameter() {
    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration parameterDeclaration = configurationDeclaration.getAllParameters().iterator().next();

    assertThat(parameterDeclaration.getName(), is("name"));
    assertThat(parameterDeclaration.getModelProperty(SyntheticModelModelProperty.class), is(notNullValue()));
  }

  @Test
  public void blocklistedConfigurationDoesntHaveNameParameter() {
    configurationDeclaration = new ExtensionDeclarer().withConfig("wsSecurity").getDeclaration();

    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionDeclaration.getConfigurations()).thenReturn(singletonList(configurationDeclaration));
    when(extensionDeclaration.getName()).thenReturn("cxf");

    enricher.enrich(extensionLoadingContext);

    assertThat(configurationDeclaration.getAllParameters().isEmpty(), is(true));
  }
}
