/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.ACCESS_TOKEN_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.AFTER_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.AUTHORIZATION_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.BEFORE_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CALLBACK_PATH_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CONSUMER_KEY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CONSUMER_SECRET_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.EXTERNAL_CALLBACK_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.LISTENER_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.LOCAL_AUTHORIZE_PATH_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_AUTHORIZATION_CODE_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_CALLBACK_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_STORE_CONFIG_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OBJECT_STORE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.RESOURCE_OWNER_ID_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.SCOPES_PARAMETER_NAME;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthModelProperty;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalConnectionProviderModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Looks for all the {@link ConnectionProviderDeclaration} with the {@link OAuthModelProperty}
 * and adds synthetic parameters that allows configuring the proper grant type
 *
 * @since 1.0
 */
public class OAuthDeclarationEnricher implements DeclarationEnricher {

  private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
  private final MetadataType stringType = typeLoader.load(String.class);

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    new IdempotentDeclarationWalker() {

      @Override
      protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {
        declaration.getModelProperty(OAuthModelProperty.class)
            .ifPresent(property -> enrich(extensionDeclaration, declaration, property.getGrantTypes()));
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void enrich(ExtensionDeclaration extension, ConnectionProviderDeclaration declaration,
                      List<OAuthGrantType> grantTypes) {
    grantTypes.forEach(type -> {
      if (AuthorizationCodeGrantType.NAME.equals(type.getName())) {
        addOAuthAuthorizationCodeParameters(declaration, (AuthorizationCodeGrantType) type);
        addOAuthCallbackParameters(declaration);
        addOAuthStoreConfigParameter(declaration);
      } else {
        throw new IllegalConnectionProviderModelDefinitionException(
                                                                    format("Extension '%s' defines connection provider '%s' with unsupported OAuth GrantType '%s'",
                                                                           extension.getName(), declaration.getName(),
                                                                           type.getName()));
      }
    });
  }

  private void addOAuthAuthorizationCodeParameters(ConnectionProviderDeclaration declaration,
                                                   AuthorizationCodeGrantType grantType) {
    List<ParameterDeclaration> params = new LinkedList<>();
    params.add(buildParameter(CONSUMER_KEY_PARAMETER_NAME, "The OAuth consumerKey as registered with the service provider",
                              true, stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(CONSUMER_SECRET_PARAMETER_NAME, "The OAuth consumerSecret as registered with the service provider",
                              true, stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(AUTHORIZATION_URL_PARAMETER_NAME, "The service provider's authorization endpoint URL",
                              false, stringType, NOT_SUPPORTED, grantType.getAuthorizationUrl()));

    params.add(buildParameter(ACCESS_TOKEN_URL_PARAMETER_NAME, "The service provider's accessToken endpoint URL",
                              false, stringType, NOT_SUPPORTED, grantType.getAccessTokenUrl()));

    params.add(buildParameter(SCOPES_PARAMETER_NAME,
                              "The OAuth scopes to be requested during the dance. If not provided, it will default "
                                  + "to those in the annotation",
                              false, stringType, NOT_SUPPORTED, grantType.getDefaultScope().orElse(null)));

    params.add(buildParameter(RESOURCE_OWNER_ID_PARAMETER_NAME, "The resourceOwnerId which each component should use "
        + "if it doesn't reference otherwise.", true, stringType, SUPPORTED, null));

    params
        .add(buildParameter(BEFORE_FLOW_PARAMETER_NAME, "The name of a flow to be executed right before starting the OAuth dance",
                            false, stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(AFTER_FLOW_PARAMETER_NAME,
                              "The name of a flow to be executed right after an accessToken has been received",
                              false, stringType, NOT_SUPPORTED, null));

    addToGroup(params, OAUTH_AUTHORIZATION_CODE_GROUP_NAME, declaration);
  }

  private void addOAuthCallbackParameters(ConnectionProviderDeclaration declaration) {
    List<ParameterDeclaration> params = new LinkedList<>();
    params.add(buildParameter(LISTENER_CONFIG_PARAMETER_NAME, "A reference to a <http:listener-config /> to be used in order "
        + "to create the listener that will catch the access token callback endpoint.", true, stringType,
                              NOT_SUPPORTED, null));

    params.add(buildParameter(CALLBACK_PATH_PARAMETER_NAME, "The path of the access token callback endpoint",
                              true, stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(LOCAL_AUTHORIZE_PATH_PARAMETER_NAME,
                              "The path of the local http endpoint which triggers the OAuth dance", true,
                              stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(EXTERNAL_CALLBACK_URL_PARAMETER_NAME, "If the callback endpoint is behind a proxy or should be "
        + "accessed through a non direct URL, use this parameter to tell the OAuth provider the URL it should use "
        + "to access the callback", false, stringType, NOT_SUPPORTED, null));

    params.add(buildParameter(LOCAL_AUTHORIZE_PATH_PARAMETER_NAME,
                              "The path of the local http endpoint which triggers the OAuth dance", true,
                              stringType, NOT_SUPPORTED, null));

    addToGroup(params, OAUTH_CALLBACK_GROUP_NAME, declaration);
  }

  private void addToGroup(List<ParameterDeclaration> params, String groupName, ConnectionProviderDeclaration declaration) {
    ParameterGroupDeclaration group = declaration.getParameterGroup(groupName);
    params.forEach(group::addParameter);
    group.showInDsl(true);
  }

  private void addOAuthStoreConfigParameter(ConnectionProviderDeclaration declaration) {
    addToGroup(asList(buildParameter(OBJECT_STORE_PARAMETER_NAME, "A reference to the object store that should be used to store "
        + "each resource owner id's data. If not specified, runtime will automatically provision the default one.",
                                     false, stringType, NOT_SUPPORTED, null)),
               OAUTH_STORE_CONFIG_GROUP_NAME, declaration);
  }

  private ParameterDeclaration buildParameter(String name, String description, boolean required, MetadataType type,
                                              ExpressionSupport expressionSupport, Object defaultValue) {
    ParameterDeclaration parameter = new ParameterDeclaration(name);
    parameter.setDescription(description);
    parameter.setExpressionSupport(expressionSupport);
    parameter.setRequired(required);
    parameter.setDefaultValue(defaultValue);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(type, false);

    return parameter;
  }
}
