/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;

import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Exception to be thrown by operations which are trying to use an expired access token.
 * <p>
 * The runtime will automatically catch this exception, try to execute the refresh token flow and retry the operation. If the
 * operation fails again or the token couldn't be refresh for whatever reason, then the operation will fail.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public class AccessTokenExpiredException extends org.mule.sdk.api.connectivity.oauth.AccessTokenExpiredException {

  /**
   * {@inheritDoc}
   *
   * @since 1.2.1
   */
  public AccessTokenExpiredException() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public AccessTokenExpiredException(String resourceOwnerId) {
    super(resourceOwnerId);
  }

}
