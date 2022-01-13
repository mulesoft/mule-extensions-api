/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.System.identityHashCode;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.CACHED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CLIENT_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CLIENT_SECRET;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CONNECTION_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.OAUTH_AUTHORIZATION_CODE_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.OAUTH_CLIENT_CREDENTIALS_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.OAUTH_PLATFORM_MANAGED_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.TOKEN_URL;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.URL_PATH;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.URL_TEMPLATE;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.ACCESS_TOKEN_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.AFTER_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.AUTHORIZATION_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.BEFORE_FLOW_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CALLBACK_PATH_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CLIENT_ID_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CLIENT_SECRET_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CONSUMER_KEY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.CONSUMER_SECRET_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.EXTERNAL_CALLBACK_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.LISTENER_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.LOCAL_AUTHORIZE_PATH_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_AUTHORIZATION_CODE_GROUP_DISPLAY_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_AUTHORIZATION_CODE_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_CALLBACK_GROUP_DISPLAY_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_CALLBACK_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_CLIENT_CREDENTIALS_GROUP_DISPLAY_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_CLIENT_CREDENTIALS_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_STORE_CONFIG_GROUP_DISPLAY_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OAUTH_STORE_CONFIG_GROUP_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.OBJECT_STORE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_DISPLAY_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.PLATFORM_MANAGED_CONNECTION_PROVIDER_DESCRIPTION;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.PLATFORM_MANAGED_CONNECTION_PROVIDER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.RESOURCE_OWNER_ID_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.SCOPES_PARAMETER_NAME;
import static org.mule.runtime.extension.api.connectivity.oauth.ExtensionOAuthConstants.TOKEN_URL_PARAMETER_NAME;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONFIG;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.FLOW;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;
import static org.mule.runtime.extension.internal.ocs.PlatformManagedOAuthUtils.isPlatformManagedOAuthEnabled;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.ClientCredentialsGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantTypeVisitor;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthModelProperty;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthParameterModelProperty;
import org.mule.runtime.extension.api.connectivity.oauth.PlatformManagedOAuthGrantType;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Looks for all the {@link ConnectionProviderDeclaration} with the {@link OAuthModelProperty} and adds synthetic parameters that
 * allows configuring the proper grant type
 *
 * @since 1.0
 */
public class OAuthDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    final Set<Integer> visitedOwners = new HashSet<>();
    final Set<Integer> visitedProviders = new HashSet<>();
    final Reference<ConnectionProviderDeclaration> ocsConnectionProvider = new Reference<>(null);
    final boolean ocsEnabled = isPlatformManagedOAuthEnabled();
    new DeclarationWalker() {

      @Override
      protected void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        declaration.getModelProperty(OAuthModelProperty.class).ifPresent(property -> {
          if (visitedProviders.add(identityHashCode(declaration))) {
            new PropertiesEnricher(declaration, property.getGrantTypes()).enrich();
            declaration.getAllParameters().forEach(parameterDeclaration -> {
              parameterDeclaration.getModelProperty(OAuthParameterModelProperty.class)
                  .ifPresent(oAuthParameterModelProperty -> parameterDeclaration.setExpressionSupport(NOT_SUPPORTED));
            });
          }

          if (visitedOwners.add(identityHashCode(owner)) && ocsEnabled) {
            addOCSConnectionProvider(owner, extensionLoadingContext);
          }
        });
      }

      private void addOCSConnectionProvider(ConnectedDeclaration owner, ExtensionLoadingContext context) {
        if (ocsConnectionProvider.get() == null) {
          ConnectionProviderDeclarer declarer =
              context.getExtensionDeclarer().withConnectionProvider(owner, PLATFORM_MANAGED_CONNECTION_PROVIDER_NAME);

          // TODO - MULE-18043: OCS Connection Provider's Management Type must be set correctly.
          final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
          declarer.withConnectionManagementType(CACHED)
              .supportsConnectivityTesting(true)
              .withSemanticTerm(OAUTH_PLATFORM_MANAGED_CONNECTION)
              .withModelProperty(new SyntheticModelModelProperty())
              .withModelProperty(new OAuthModelProperty(singletonList(new PlatformManagedOAuthGrantType())))
              .describedAs(PLATFORM_MANAGED_CONNECTION_PROVIDER_DESCRIPTION)
              .onDefaultParameterGroup().withRequiredParameter(PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_NAME)
              .describedAs(PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_DESCRIPTION)
              .ofType(typeLoader.load(String.class))
              .withExpressionSupport(NOT_SUPPORTED)
              .withRole(BEHAVIOUR)
              .withSemanticTerm(CONNECTION_ID)
              .withDisplayModel(DisplayModel.builder()
                  .displayName(PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_DISPLAY_NAME)
                  .example("ocs:348573-495273958273-924852945/salesforce/john-sfdc-1k87kmjt")
                  .summary(PLATFORM_MANAGED_CONNECTION_ID_PARAMETER_DESCRIPTION)
                  .build());
          ocsConnectionProvider.set(declarer.getDeclaration());
        } else {
          owner.addConnectionProvider(ocsConnectionProvider.get());
        }
      }
    }.walk(extensionDeclaration);
  }

  private class PropertiesEnricher implements OAuthGrantTypeVisitor {

    private final ConnectionProviderDeclaration declaration;
    private final List<OAuthGrantType> grantTypes;

    private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    private final MetadataType stringType = typeLoader.load(String.class);

    private PropertiesEnricher(ConnectionProviderDeclaration declaration, List<OAuthGrantType> grantTypes) {
      this.declaration = declaration;
      this.grantTypes = grantTypes;
    }

    private void enrich() {
      grantTypes.forEach(type -> type.accept(this));
    }

    @Override
    public void visit(AuthorizationCodeGrantType grantType) {
      declaration.addSemanticTerm(OAUTH_AUTHORIZATION_CODE_CONNECTION);
      addOAuthAuthorizationCodeParameters(declaration, grantType);
      addOAuthCallbackParameters(declaration);
      addOAuthStoreConfigParameter(declaration);
    }

    @Override
    public void visit(ClientCredentialsGrantType grantType) {
      declaration.addSemanticTerm(OAUTH_CLIENT_CREDENTIALS_CONNECTION);
      addOAuthClientCredentialsParameters(declaration, grantType);
      addOAuthStoreConfigParameter(declaration);
    }

    @Override
    public void visit(PlatformManagedOAuthGrantType grantType) {
      // This grant type functions over completely synthetic connection providers
    }

    private void addOAuthClientCredentialsParameters(ConnectionProviderDeclaration declaration,
                                                     ClientCredentialsGrantType grantType) {
      List<ParameterDeclaration> params = new LinkedList<>();
      params.add(buildParameter(CLIENT_ID_PARAMETER_NAME, "The OAuth client id as registered with the service provider",
                                true, stringType, SUPPORTED, null, CLIENT_ID));

      params
          .add(buildParameter(CLIENT_SECRET_PARAMETER_NAME, "The OAuth client secret as registered with the service provider",
                              true, stringType, SUPPORTED, null, CLIENT_SECRET));

      params.add(buildParameter(TOKEN_URL_PARAMETER_NAME, "The service provider's token endpoint URL",
                                false, stringType, SUPPORTED, grantType.getTokenUrl(), TOKEN_URL));

      params.add(buildParameter(SCOPES_PARAMETER_NAME,
                                "The OAuth scopes to be requested during the dance. If not provided, it will default "
                                    + "to those in the annotation",
                                false, stringType, SUPPORTED, grantType.getDefaultScopes().orElse(null)));

      addToGroup(params, OAUTH_CLIENT_CREDENTIALS_GROUP_NAME, OAUTH_CLIENT_CREDENTIALS_GROUP_DISPLAY_NAME, declaration);
    }

    private void addOAuthAuthorizationCodeParameters(ConnectionProviderDeclaration declaration,
                                                     AuthorizationCodeGrantType grantType) {
      List<ParameterDeclaration> params = new LinkedList<>();
      params.add(buildParameter(CONSUMER_KEY_PARAMETER_NAME, "The OAuth consumerKey as registered with the service provider",
                                true, stringType, NOT_SUPPORTED, null, CLIENT_ID));

      params
          .add(buildParameter(CONSUMER_SECRET_PARAMETER_NAME, "The OAuth consumerSecret as registered with the service provider",
                              true, stringType, NOT_SUPPORTED, null, CLIENT_SECRET));

      params.add(buildParameter(AUTHORIZATION_URL_PARAMETER_NAME, "The service provider's authorization endpoint URL",
                                false, stringType, NOT_SUPPORTED, grantType.getAuthorizationUrl(),
                                AUTHORIZATION_URL_PARAMETER_NAME));

      params.add(buildParameter(ACCESS_TOKEN_URL_PARAMETER_NAME, "The service provider's accessToken endpoint URL",
                                false, stringType, NOT_SUPPORTED, grantType.getAccessTokenUrl(), TOKEN_URL));

      params.add(buildParameter(SCOPES_PARAMETER_NAME,
                                "The OAuth scopes to be requested during the dance. If not provided, it will default "
                                    + "to those in the annotation",
                                false, stringType, NOT_SUPPORTED, grantType.getDefaultScope().orElse(null)));

      params.add(buildParameter(RESOURCE_OWNER_ID_PARAMETER_NAME, "The resourceOwnerId which each component should use "
          + "if it doesn't reference otherwise.", false, stringType, SUPPORTED, null, CLIENT_ID));

      final ParameterDeclaration beforeFlowParam = buildParameter(BEFORE_FLOW_PARAMETER_NAME,
                                                                  "The name of a flow to be executed right before starting the OAuth dance",
                                                                  false, stringType, NOT_SUPPORTED, null);
      beforeFlowParam.setAllowedStereotypeModels(singletonList(FLOW));
      params.add(beforeFlowParam);

      final ParameterDeclaration afterFlowParam = buildParameter(AFTER_FLOW_PARAMETER_NAME,
                                                                 "The name of a flow to be executed right after an accessToken has been received",
                                                                 false, stringType, NOT_SUPPORTED, null);
      afterFlowParam.setAllowedStereotypeModels(singletonList(FLOW));
      params.add(afterFlowParam);

      addToGroup(params, OAUTH_AUTHORIZATION_CODE_GROUP_NAME, OAUTH_AUTHORIZATION_CODE_GROUP_DISPLAY_NAME, declaration);
    }

    private void addOAuthCallbackParameters(ConnectionProviderDeclaration declaration) {
      List<ParameterDeclaration> params = new LinkedList<>();
      ParameterDeclaration listenerConfig = buildParameter(LISTENER_CONFIG_PARAMETER_NAME,
                                                           "A reference to a <http:listener-config /> to be used in order to create the "
                                                               + "listener that will catch the access token callback endpoint.",
                                                           true, stringType, NOT_SUPPORTED, null);
      listenerConfig.setAllowedStereotypeModels(singletonList(newStereotype("LISTENER_CONFIG", "HTTP")
          .withParent(CONFIG)
          .build()));
      params.add(listenerConfig);

      params.add(buildParameter(CALLBACK_PATH_PARAMETER_NAME, "The path of the access token callback endpoint",
                                true, stringType, NOT_SUPPORTED, null, URL_PATH));

      params.add(buildParameter(LOCAL_AUTHORIZE_PATH_PARAMETER_NAME,
                                "The path of the local http endpoint which triggers the OAuth dance", true,
                                stringType, NOT_SUPPORTED, null, URL_PATH));

      params.add(buildParameter(EXTERNAL_CALLBACK_URL_PARAMETER_NAME, "If the callback endpoint is behind a proxy or should be "
          + "accessed through a non direct URL, use this parameter to tell the OAuth provider the URL it should use "
          + "to access the callback", false, stringType, NOT_SUPPORTED, null, URL_TEMPLATE));

      addToGroup(params, OAUTH_CALLBACK_GROUP_NAME, OAUTH_CALLBACK_GROUP_DISPLAY_NAME, declaration);
    }

    private void addToGroup(List<ParameterDeclaration> params, String groupName, String displayGroupName,
                            ConnectionProviderDeclaration declaration) {
      ParameterGroupDeclaration group = declaration.getParameterGroup(groupName);
      group.setDisplayModel(DisplayModel.builder().displayName(displayGroupName).build());
      params.forEach(group::addParameter);
      group.showInDsl(true);
    }

    private void addOAuthStoreConfigParameter(ConnectionProviderDeclaration declaration) {
      final ParameterDeclaration osParameter = buildParameter(
                                                              OBJECT_STORE_PARAMETER_NAME,
                                                              "A reference to the object store that should be used to store " +
                                                                  "each resource owner id's data. If not specified, runtime will automatically provision the default one.",
                                                              false, stringType, NOT_SUPPORTED, null);
      osParameter.setAllowedStereotypeModels(singletonList(OBJECT_STORE));
      addToGroup(asList(osParameter), OAUTH_STORE_CONFIG_GROUP_NAME, OAUTH_STORE_CONFIG_GROUP_DISPLAY_NAME, declaration);
    }

    private ParameterDeclaration buildParameter(String name, String description, boolean required, MetadataType type,
                                                ExpressionSupport expressionSupport, Object defaultValue,
                                                String... semanticTerms) {
      ParameterDeclaration parameter = new ParameterDeclaration(name);
      parameter.setDescription(description);
      parameter.setExpressionSupport(expressionSupport);
      parameter.setRequired(required);
      parameter.setDefaultValue(defaultValue);
      parameter.setParameterRole(BEHAVIOUR);
      parameter.setType(type, false);
      if (semanticTerms != null) {
        stream(semanticTerms).forEach(parameter::addSemanticTerm);
      }
      return parameter;
    }
  }
}
