/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;
import org.mule.runtime.api.exception.MuleRuntimeException;

/**
 * Exception to be thrown by operations which are trying to use an expired access token.
 *
 * The runtime will automatically catch this exception, try to execute the refresh token flow
 * and retry the operation. If the operation fails again or the token couldn't be refresh for
 * whatever reason, then the operation will fail
 *
 * @since 1.0
 */
public class AccessTokenExpiredException extends MuleRuntimeException {

  private final String resourceOwnerId;

  /**
   * Creates a new instance
   *
   * @param resourceOwnerId the id of the resource owner which access token expired.
   */
  public AccessTokenExpiredException(String resourceOwnerId) {
    super(createStaticMessage("Access Token expired for resource owner id " + resourceOwnerId));
    this.resourceOwnerId = resourceOwnerId;
  }

  /**
   * @return the id of the resource owner which access token expired.
   */
  public String getResourceOwnerId() {
    return resourceOwnerId;
  }
}
