/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import java.util.Optional;

/**
 * An  object which holds information about an OAuth authorization
 *
 * @since 1.2.1
 */
public interface OAuthState {

  /**
   * @return The obtained access token
   */
  String getAccessToken();

  /**
   * @return The access token's expiration. The actual format of it depends on the OAuth provider
   */
  Optional<String> getExpiresIn();
}
