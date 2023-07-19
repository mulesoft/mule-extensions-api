/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.runtime.connectivity.Reconnectable;
import org.mule.runtime.extension.api.runtime.connectivity.ReconnectionCallback;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * A callback to allow {@link Source} implementations to communicate generated messages back to the runtime so that they can be
 * processed.
 * <p>
 * Those messages will be represented as {@link Result} objects using the {@link #handle(Result)} or
 * {@link #handle(Result, SourceCallbackContext)} methods.
 *
 * @param <T> the generic type of the output values of the generated results
 * @param <A> the generic type of the attributes of the generated results
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface SourceCallback<T, A> {

  /**
   * Passes the given {@code result} back to the runtime for processing.
   *
   * @param result a {@link Result}
   */
  void handle(Result<T, A> result);

  /**
   * Passes the given {@code result} back to the runtime for processing. This method also allows providing a
   * {@link SourceCallbackContext} so that state can be associated to each {@code result}.
   * <p>
   * This is specially helpful for sources which emits responses on stateful connections or which require to pass back some kind
   * of token or identifier. Notice that this is not the only possible use case though.
   * <p>
   * The {@code context} instance should be created using the {@link #createContext()} of {@code this} same instance
   *
   * @param result  a {@link Result}
   * @param context context a {@link SourceCallbackContext}
   */
  void handle(Result<T, A> result, SourceCallbackContext context);

  /**
   * Any started {@link Source} must use this method to communicate the runtime that a {@link ConnectionException}was found trying
   * to produce messages.
   * <p>
   * This callback is to be used for connectivity errors that happen <b>after</b> {@link Source#onStart(SourceCallback)} has
   * successfully returned (otherwise, you can simply throw the exception there).
   *
   * When notified of the exception through this method, the runtime will decide if reconnection is to be attempted, depending on
   * the configuration. If the related {@link Source} implements the {@link Reconnectable} interface, then
   * {@link Reconnectable#reconnect(ConnectionException, ReconnectionCallback)} will be invoked, otherwise the runtime will try to
   * reestablish connection by restarting the owning {@link Source}. In case that the {@link ConnectionException} communicates
   * which is the connection with connectivity issues, the runtime will invalidate it, disconnecting and destroying it.
   *
   * @param e the {@link ConnectionException} we need to recover from.
   */
  void onConnectionException(ConnectionException e);

  /**
   * @return a new instance of {@link SourceCallbackContext}, only valid for use on {@code this} same {@link SourceCallback}
   *         instance
   */
  SourceCallbackContext createContext();
}
