/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

/**
 * {@link MessageDispatcherProvider} that creates instances of {@link MessageDispatcher} using a
 * a {@link DispatchingContext}.
 *
 * @since 1.1
 */
public abstract class ContextAwareMessageDispatcherProvider<T extends MessageDispatcher> implements MessageDispatcherProvider<T> {

  /**
   * @param client a well initialized extensions client, ready to execute operations.
   * @return a new {@link MessageDispatcher} that uses an {@link ExtensionsClient}.
   */
  public abstract T connect(DispatchingContext client);

  /**
   * This method should not be used, instead use {@link ContextAwareMessageDispatcherProvider#connect(DispatchingContext)}.
   * <p>
   * If an extension client is not required then only implements {@link MessageDispatcherProvider}.
   */
  @Override
  final public T connect() throws ConnectionException {
    throw new UnsupportedOperationException("ContextAwareMessageDispatcherProvider does not support parameterless connect");
  }
}
