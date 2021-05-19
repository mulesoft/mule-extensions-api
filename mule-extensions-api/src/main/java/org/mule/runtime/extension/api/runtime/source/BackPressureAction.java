/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;

/**
 * Indicates which type of back pressure has the runtime applied on a give message.
 *
 * Notice that if compared to {@link BackPressureMode}, there's no equivalent to {@link BackPressureMode#WAIT}, that's because the
 * {@link BackPressureMode#WAIT} mode is simply about blocking executions of {@link SourceCallback#handle(Result)}
 *
 * @since 1.1
 */
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
