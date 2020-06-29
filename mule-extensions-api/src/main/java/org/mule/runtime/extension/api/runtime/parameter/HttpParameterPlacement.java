/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.parameter;

/**
 * Describes the possible placements a parameter can have inside a HTTP request
 *
 * @since 1.2.1
 * @deprecated use {@link org.mule.sdk.api.runtime.parameter.HttpParameterPlacement} instead.
 */
@Deprecated
public enum HttpParameterPlacement {

  /**
   * The parameter will be sent as a query parameter
   */
  QUERY_PARAMS,

  /**
   * The parameter will be sent as a header
   */
  HEADERS
}
