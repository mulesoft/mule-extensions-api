/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.security;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Describes the possible placements credentials can have inside a HTTP request
 *
 * @since 1.2.1
 */
@MinMuleVersion("4.2.1")
public enum CredentialsPlacement {

  /**
   * Send credentials in a Basic Authentication header
   */
  BASIC_AUTH_HEADER,

  /**
   * Send credentials encoded in HTTP Body
   */
  BODY,

  /**
   * Send credentials as query parameters
   */
  QUERY_PARAMS
}
