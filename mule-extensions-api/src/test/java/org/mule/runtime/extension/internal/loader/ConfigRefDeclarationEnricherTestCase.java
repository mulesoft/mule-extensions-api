/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;

import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.NoImplicitModelProperty;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;
import org.mule.runtime.extension.internal.loader.enricher.ConfigRefDeclarationEnricher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigRefDeclarationEnricherTestCase {

  private static final String CONFIG_REF_NAME = "config-ref";

  @Mock
  private ExtensionLoadingContext extensionLoadingContext;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclarer extensionDeclarer;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclaration extensionDeclaration;

  private ConfigRefDeclarationEnricher enricher = new ConfigRefDeclarationEnricher();

  private OperationDeclaration operationDeclaration;

  private ConfigurationDeclaration configurationDeclaration;

  @Before
  public void before() {
    operationDeclaration = spy(new ExtensionDeclarer().withOperation("testOperation").getDeclaration());
    configurationDeclaration = spy(new ExtensionDeclarer().withConfig("testConfiguration")).getDeclaration();

    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionDeclaration.getOperations()).thenReturn(singletonList(operationDeclaration));
    when(extensionDeclaration.getConfigurations()).thenReturn(singletonList(configurationDeclaration));
  }

  @Test
  public void enrichedComponentModelHasConfigRefParameterWhenComponentHasConfigurationAssociated() {
    configurationDeclaration.addOperation(operationDeclaration);

    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration parameterDeclaration = operationDeclaration.getAllParameters().iterator().next();
    assertThat(parameterDeclaration.getName(), equalTo(CONFIG_REF_NAME));
    assertThat(parameterDeclaration.getModelProperty(SyntheticModelModelProperty.class), is(notNullValue()));
  }

  @Test
  public void enrichedComponentModelDoesNotHaveConfigRefParameterWhenComponentDoesNotHaveConfigurationAssociated() {
    enricher.enrich(extensionLoadingContext);

    assertThat(operationDeclaration.getAllParameters(), is(empty()));
  }

  @Test
  public void nonImplicitConfigHasRequiredConfigRef() {
    configurationDeclaration.addOperation(operationDeclaration);
    ParameterDeclaration param = new ParameterDeclaration("required");
    param.setRequired(true);
    configurationDeclaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(param);

    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration configRefDeclaration = operationDeclaration.getAllParameters().iterator().next();
    assertThat(configRefDeclaration.getName(), equalTo(CONFIG_REF_NAME));
    assertThat(configRefDeclaration.isRequired(), is(true));
  }

  @Test
  public void implicitConfigHasOptionalConfigRef() {
    configurationDeclaration.addOperation(operationDeclaration);
    configurationDeclaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(new ParameterDeclaration("optional"));

    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration configRefDeclaration = operationDeclaration.getAllParameters().iterator().next();
    assertThat(configRefDeclaration.getName(), equalTo(CONFIG_REF_NAME));
    assertThat(configRefDeclaration.isRequired(), is(false));
  }

  @Test
  public void forcedImplicitConfigHasRequiredConfigRef() {
    configurationDeclaration.addOperation(operationDeclaration);
    ParameterDeclaration param = new ParameterDeclaration("required");
    param.setRequired(true);
    configurationDeclaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(param);
    configurationDeclaration.addModelProperty(new NoImplicitModelProperty());

    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration configRefDeclaration = operationDeclaration.getAllParameters().iterator().next();
    assertThat(configRefDeclaration.getName(), equalTo(CONFIG_REF_NAME));
    assertThat(configRefDeclaration.isRequired(), is(true));
  }
}
