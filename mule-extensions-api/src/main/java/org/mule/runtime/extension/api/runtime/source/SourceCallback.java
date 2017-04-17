/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.runtime.operation.Result;

/**
 * A callback to allow {@link Source} implementations to communicate
 * generated messages back to the runtime so that they can be processed.
 * <p>
 * Those messages will be represented as {@link Result} objects
 * using the {@link #handle(Result)} or {@link #handle(Result, SourceCallbackContext)}
 * methods.
 *
 * @param <T> the generic type of the output values of the generated results
 * @param <A> the generic type of the attributes of the generated results
 * @since 1.0
 */
public interface SourceCallback<T, A> {

  /**
   * Passes the given {@code result} back to the runtime for processing.
   *
   * @param result a {@link Result}
   */
  void handle(Result<T, A> result);

  /**
   * Passes the given {@code result} back to the runtime for processing.
   * This method also allows providing a {@link SourceCallbackContext}
   * so that state can be associated to each {@code result}.
   * <p>
   * This is specially helpful for sources which emits responses on stateful
   * connections or which require to pass back some kind of token or identifier.
   * Notice that this is not the only possible use case though.
   * <p>
   * The {@code context} instance should be created using the
   * {@link #createContext()} of {@code this} same instance
   *
   * @param result  a {@link Result}
   * @param context context a {@link SourceCallbackContext}
   */
  void handle(Result<T, A> result, SourceCallbackContext context);

  /**
   * Any started {@link Source} must use this method to communicate the runtime that
   * an exception was found trying to produce messages. Any exceptions that the source
   * encounters trying to produce messages needs to be channeled through this method
   * and <b>NOT</b> be thrown.
   * <p>
   * Do not confuse the concept of 'exception found producing a message' from a
   * message which failed to be processed by the flow or an exception thrown by
   * a method annotated with {@link OnSuccess} or {@link OnError}. Any of those
   * represent an expected processing failure and need to be treated accordingly
   * by the source itself. Those are not something that the runtime should be
   * notified about.
   *
   * @param t a {@link Throwable}
   */
  void onSourceException(Throwable t);

  /**
   * @return a new instance of {@link SourceCallbackContext}, only
   * valid for use on {@code this} same {@link SourceCallback} instance
   */
  SourceCallbackContext createContext();
}
