/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Indicates which type of back pressure has the runtime applied on a give message.
 *
 * Notice that if compared to {@link BackPressureMode}, there's no equivalent to {@link BackPressureMode#WAIT}, that's because the
 * {@link BackPressureMode#WAIT} mode is simply about blocking executions of {@link SourceCallback#handle(Result)}
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
public enum BackPressureAction {

  /**
   * Indicates that en error has been thrown as the result of the back pressure
   */
  FAIL,

  /**
   * Indicates that the message will simply be dropped.
   */
  DROP
}
