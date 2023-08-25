/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
