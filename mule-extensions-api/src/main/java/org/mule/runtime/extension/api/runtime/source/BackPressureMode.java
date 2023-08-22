/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Describes the available back pressure strategies
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
public enum BackPressureMode {

  /**
   * Reject the event by throwing an error
   */
  FAIL,

  /**
   * Apply back pressure by blocking the {@link SourceCallback#handle(Result)} method
   */
  WAIT,

  /**
   * Apply back pressure by dropping the filtered event
   */
  DROP
}
