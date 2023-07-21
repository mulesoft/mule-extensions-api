/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Describes the possible placements a parameter can have inside a HTTP request
 *
 * @since 1.2.1
 */
@MinMuleVersion("4.2.1")
public enum HttpParameterPlacement {

  /**
   * The parameter will be sent as a query parameter
   */
  @MinMuleVersion("4.2.1")
  QUERY_PARAMS,

  /**
   * The parameter will be sent as a header
   */
  @MinMuleVersion("4.2.1")
  HEADERS,

  /**
   * The parameter will be added as part of an {@code application/x-www-form-urlencoded} Http request body
   */
  @MinMuleVersion("4.5.0")
  BODY
}
