/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.AFTER_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.BEFORE_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.LISTENER_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OBJECT_STORE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.security.CredentialsPlacement.BODY;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONFIG;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.FLOW;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;

import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.ClientCredentialsGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthModelProperty;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.OAuthDeclarationEnricher;

import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.Issue;

public class OAuthDeclarationEnricherTestCase {

  private ExtensionLoadingContext extensionLoadingContext;
  private ExtensionDeclaration extensionDeclaration;
  private ExtensionDeclarer extensionDeclarer;
  private ConnectionProviderDeclaration connectionProviderDeclaration;

  private ParameterGroupDeclaration oauthAuthorizationCodeGroupDeclaration;
  private ParameterGroupDeclaration oauthCallbackConfigGroupDeclaration;
  private ParameterGroupDeclaration oauthStoreConfigGroupDeclaration;
  private ParameterGroupDeclaration oauthClientCredentialsGroupDeclaration;

  private OAuthDeclarationEnricher enricher;

  @Before
  public void before() {
    extensionLoadingContext = mock(ExtensionLoadingContext.class);

    extensionDeclaration = mock(ExtensionDeclaration.class);

    extensionDeclarer = mock(ExtensionDeclarer.class);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);

    connectionProviderDeclaration = mock(ConnectionProviderDeclaration.class);
    oauthAuthorizationCodeGroupDeclaration = new ParameterGroupDeclaration("oauthAuthorizationCode");
    when(connectionProviderDeclaration.getParameterGroup("oauthAuthorizationCode"))
        .thenReturn(oauthAuthorizationCodeGroupDeclaration);
    oauthCallbackConfigGroupDeclaration = new ParameterGroupDeclaration("oauthCallbackConfig");
    when(connectionProviderDeclaration.getParameterGroup("oauthCallbackConfig"))
        .thenReturn(oauthCallbackConfigGroupDeclaration);
    oauthStoreConfigGroupDeclaration = new ParameterGroupDeclaration("oauthStoreConfig");
    when(connectionProviderDeclaration.getParameterGroup("oauthStoreConfig"))
        .thenReturn(oauthStoreConfigGroupDeclaration);
    oauthClientCredentialsGroupDeclaration = new ParameterGroupDeclaration("oauthClientCredentials");
    when(connectionProviderDeclaration.getParameterGroup("oauthClientCredentials"))
        .thenReturn(oauthClientCredentialsGroupDeclaration);
    when(extensionDeclaration.getConnectionProviders()).thenReturn(singletonList(connectionProviderDeclaration));

    enricher = new OAuthDeclarationEnricher();
  }

  @Test
  @Issue("MULE-18528")
  public void stereotypesForAuthorizationCode() {
    when(connectionProviderDeclaration.getModelProperty(OAuthModelProperty.class))
        .thenReturn(of(new OAuthModelProperty(asList(new AuthorizationCodeGrantType("http://accessToken",
                                                                                    "http://authorizationUrl",
                                                                                    "#[accessToken]",
                                                                                    ".*",
                                                                                    "#[refreshToken]",
                                                                                    "scope")))));

    enricher.enrich(extensionLoadingContext);

    final ParameterDeclaration listenerConfigParam = oauthCallbackConfigGroupDeclaration.getParameters()
        .stream()
        .filter(p -> p.getName().equals(LISTENER_CONFIG_PARAMETER_NAME))
        .findFirst()
        .get();

    assertThat(listenerConfigParam.getAllowedStereotypeModels(),
               hasItem(newStereotype("LISTENER_CONFIG", "HTTP")
                   .withParent(CONFIG)
                   .build()));

    final ParameterDeclaration beforeFlowParam = oauthAuthorizationCodeGroupDeclaration.getParameters()
        .stream()
        .filter(p -> p.getName().equals(BEFORE_FLOW_PARAMETER_NAME))
        .findFirst()
        .get();

    assertThat(beforeFlowParam.getAllowedStereotypeModels(),
               hasItem(FLOW));

    final ParameterDeclaration afterFlowParam = oauthAuthorizationCodeGroupDeclaration.getParameters()
        .stream()
        .filter(p -> p.getName().equals(AFTER_FLOW_PARAMETER_NAME))
        .findFirst()
        .get();

    assertThat(afterFlowParam.getAllowedStereotypeModels(),
               hasItem(FLOW));

  }

  @Test
  public void stereotypesForObjectStore() {
    when(connectionProviderDeclaration.getModelProperty(OAuthModelProperty.class))
        .thenReturn(of(new OAuthModelProperty(asList(new ClientCredentialsGrantType("http://accessToken",
                                                                                    "#[accessToken]",
                                                                                    ".*",
                                                                                    "scope",
                                                                                    BODY)))));

    enricher.enrich(extensionLoadingContext);

    final ParameterDeclaration afterFlowParam = oauthStoreConfigGroupDeclaration.getParameters()
        .stream()
        .filter(p -> p.getName().equals(OBJECT_STORE_PARAMETER_NAME))
        .findFirst()
        .get();

    assertThat(afterFlowParam.getAllowedStereotypeModels(),
               hasItem(OBJECT_STORE));
  }
}
