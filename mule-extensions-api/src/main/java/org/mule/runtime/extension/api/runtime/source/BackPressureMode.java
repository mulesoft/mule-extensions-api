/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
