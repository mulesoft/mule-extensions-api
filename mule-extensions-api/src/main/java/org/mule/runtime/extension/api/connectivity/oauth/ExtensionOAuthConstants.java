/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;

/**
 * Constants for the Extensions API OAuth support
 *
 * @since 1.0
 */
public final class ExtensionOAuthConstants {

  /**
   * The name of the {@link ParameterGroupModel} in which all the Authorization-Code grant type
   * related parameters are to be placed.
   */
  public static final String OAUTH_AUTHORIZATION_CODE_GROUP_NAME = "oauthAuthorizationCode";

  /**
   * The name of the {@link ParameterGroupModel} in which all the Authorization-Code grant type
   * related parameters are to be placed.
   */
  public static final String OAUTH_CLIENT_CREDENTIALS_GROUP_NAME = "oauthClientCredentials";

  /**
   * The display name of the {@link ParameterGroupModel} in which all the Authorization-Code grant type
   * related parameters are to be placed.
   */
  public static final String OAUTH_AUTHORIZATION_CODE_GROUP_DISPLAY_NAME = "OAuth Authorization Code";

  /**
   * The display name of the {@link ParameterGroupModel} in which all the Client Credentials grant type
   * related parameters are to be placed.
   */
  public static final String OAUTH_CLIENT_CREDENTIALS_GROUP_DISPLAY_NAME = "OAuth Client Credentials";

  /**
   * The name of the {@link ParameterGroupModel} in which all the OAuth callback
   * related parameters are to be placed.
   */
  public static final String OAUTH_CALLBACK_GROUP_NAME = "oauthCallbackConfig";
  /**
   * The display name of the {@link ParameterGroupModel} in which all the OAuth callback
   * related parameters are to be placed.
   */
  public static final String OAUTH_CALLBACK_GROUP_DISPLAY_NAME = "OAuth Callback Config";

  /**
   * The name of the {@link ParameterGroupModel} in which all the store config
   * related parameters are to be placed.
   */
  public static final String OAUTH_STORE_CONFIG_GROUP_NAME = "oauthStoreConfig";

  /**
   * The display name of the {@link ParameterGroupModel} in which all the store config
   * related parameters are to be placed.
   */
  public static final String OAUTH_STORE_CONFIG_GROUP_DISPLAY_NAME = "OAuth Store Config";

  /**
   * The name of the parameter on which the OAuth consumer key is configured
   */
  public static final String CONSUMER_KEY_PARAMETER_NAME = "consumerKey";

  /**
   * The name of the parameter on which the OAuth consumer secret is configured
   */
  public static final String CONSUMER_SECRET_PARAMETER_NAME = "consumerSecret";

  /**
   * The name of the parameter on which the OAuth client id is configured
   */
  public static final String CLIENT_ID_PARAMETER_NAME = "clientId";

  /**
   * The name of the parameter on which the OAuth client secret is configured
   */
  public static final String CLIENT_SECRET_PARAMETER_NAME = "clientSecret";

  /**
   * The name of the parameter on which the authorization Url is configured
   */
  public static final String AUTHORIZATION_URL_PARAMETER_NAME = "authorizationUrl";

  /**
   * The name of the parameter on which the access token Url is configured
   */
  public static final String ACCESS_TOKEN_URL_PARAMETER_NAME = "accessTokenUrl";

  /**
   * The name of the parameter on which the OAuth token Url is configured
   */
  public static final String TOKEN_URL_PARAMETER_NAME = "tokenUrl";

  /**
   * The name of the parameter on which the scopes are configured
   */
  public static final String SCOPES_PARAMETER_NAME = "scopes";

  /**
   * The name of the parameter on which the id of the user to be authenticated is configured
   */
  public static final String RESOURCE_OWNER_ID_PARAMETER_NAME = "resourceOwnerId";

  /**
   * The name of the parameter on which the before flow is configured
   */
  public static final String BEFORE_FLOW_PARAMETER_NAME = "before";

  /**
   * The name of the parameter on which the after flow is configured
   */
  public static final String AFTER_FLOW_PARAMETER_NAME = "after";

  /**
   * The name of the parameter on which a custom Http listener is configured
   */
  public static final String LISTENER_CONFIG_PARAMETER_NAME = "listenerConfig";

  /**
   * The name of the parameter on which the path of the OAuth callback is configured
   */
  public static final String CALLBACK_PATH_PARAMETER_NAME = "callbackPath";

  /**
   * The name of the parameter on which the external callback URL is configured
   */
  public static final String EXTERNAL_CALLBACK_URL_PARAMETER_NAME = "externalCallbackUrl";

  /**
   * The name of the parameter on which the path of the local authorization url is configured
   */
  public static final String LOCAL_AUTHORIZE_PATH_PARAMETER_NAME = "authorizePath";

  /**
   * The name of the parameter on which the name of a custom object store is configured
   */
  public static final String OBJECT_STORE_PARAMETER_NAME = "objectStore";

  /**
   * The name of the synthetic operation used to invalidate authorizations.
   */
  public static final String UNAUTHORIZE_OPERATION_NAME = "unauthorize";

  private ExtensionOAuthConstants() {}
}
