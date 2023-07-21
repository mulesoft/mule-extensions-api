/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

/**
 * {@link ConnectionProvider} specialization that handles {@link MessageDispatcher} connections that are bundled to a
 * {@link SoapServiceProvider}, which are created from when the {@link SoapServiceProvider} is created.
 *
 * @since 1.0
 */
public interface MessageDispatcherProvider<T extends MessageDispatcher> extends ConnectionProvider<T> {

  /**
   * Validates the given {@link T}.
   *
   * In invalid connection case, the {@link ConnectionValidationResult} should also return a valid message
   * {@link ConnectionValidationResult#getMessage()}, exception {@link ConnectionValidationResult#getException()} and code
   * {@link ConnectionValidationResult#getErrorType()}
   *
   * @param connection a non {@code null} {@link T}.
   * @return a {@link ConnectionValidationResult} indicating if the connection is valid or not.
   *
   * @since 1.1
   */
  default ConnectionValidationResult validate(T connection, SoapServiceProvider provider) {
    return ConnectionValidationResult.success();
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link MessageDispatcherProvider#validate(MessageDispatcher, SoapServiceProvider)}
   */
  @Deprecated
  @Override
  default ConnectionValidationResult validate(T connection) {
    return ConnectionValidationResult.success();
  }
}
