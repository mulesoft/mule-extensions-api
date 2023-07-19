/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

/**
 * {@link MessageDispatcherProvider} that creates instances of {@link MessageDispatcher} using a a {@link DispatchingContext}.
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
