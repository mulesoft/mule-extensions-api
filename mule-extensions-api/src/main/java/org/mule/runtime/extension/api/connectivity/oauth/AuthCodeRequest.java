/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.runtime.api.connection.ConnectionProvider;

import java.util.Optional;

/**
 * Provides information about the request that is sent to the authorization url
 * during an OAuth2 dance using the Authorization-Code grant type.
 * <p>
 * If the acting {@link ConnectionProvider} has a {@code before} flow configured, then
 * an instance of this class will be initial payload of such flow.
 *
 * @since 1.0
 */
public interface AuthCodeRequest {

  /**
   * @return The id of the user being authenticated
   */
  String getResourceOwnerId();

  /**
   * @return The scopes that were requested
   */
  Optional<String> getScopes();

  /**
   * @return The OAuth state that was sent
   */
  Optional<String> getState();

  /**
   * @return The external callback url that the user configured or {@link Optional#empty()} if none was provided
   */
  Optional<String> getExternalCallbackUrl();
}
