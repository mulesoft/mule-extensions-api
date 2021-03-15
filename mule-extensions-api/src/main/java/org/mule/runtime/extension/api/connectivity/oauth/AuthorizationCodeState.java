/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;


import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.AuthorizationCode;

import java.util.Optional;

/**
 * {@link ConnectionProvider} implementations which are also annotated with {@link AuthorizationCode} <b>MUST</b> have a field of
 * this type. When the authorization dance is completed, the runtime will inject in such field an instance of this class.
 *
 * This class holds all the relevant information about the completed authorization dance so that the {@link ConnectionProvider}
 * can make use of it.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.connectivity.oauth.AuthorizationCodeState} instead.
 */
@NoImplement
@Deprecated
public interface AuthorizationCodeState extends OAuthState {

  /**
   * @return The obtained refresh token
   */
  Optional<String> getRefreshToken();

  /**
   * @return The id of the user that was authenticated
   */
  String getResourceOwnerId();

  /**
   * @return The OAuth state that was originally sent
   */
  Optional<String> getState();

  /**
   * @return The url of the authorization endpoint that was used in the authorization process
   */
  String getAuthorizationUrl();

  /**
   * @return The url of the access token endpoint that was used in the authorization process
   */
  String getAccessTokenUrl();

  /**
   * @return The OAuth consumer key that was used in the authorization process
   */
  String getConsumerKey();

  /**
   * @return The OAuth consumer secret that was used in the authorization process
   */
  String getConsumerSecret();

  /**
   * @return The external callback url that the user configured or {@link Optional#empty()} if none was provided
   */
  Optional<String> getExternalCallbackUrl();
}
