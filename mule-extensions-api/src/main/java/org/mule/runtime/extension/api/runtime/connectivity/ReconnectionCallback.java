/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.connectivity;

import org.mule.runtime.api.connection.ConnectionException;

/**
 * Callback used to notify the runtime about the outcome of a reconnection attempt.
 *
 * @since 1.0
 */
public interface ReconnectionCallback {

  /**
   * Notifies the runtime that the connection was successfully reestablished
   */
  void success();

  /**
   * Notifies the runtime that the reconnection attempt failed because of the given {@code exception}
   *
   * @param exception the {@link ConnectionException} for which the reconnection failed
   */
  void failed(ConnectionException exception);
}
