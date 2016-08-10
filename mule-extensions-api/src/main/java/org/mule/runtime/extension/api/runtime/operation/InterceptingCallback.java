/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.message.MuleMessage;

/**
 * An operation is intercepting when its return type matches this class.
 * <p>
 * The {@link #getResult()} method gives access to the intercepting operation
 * actual return type, while the other methods are use for getting notifications
 * about the execution of the intercepted chain.
 * <p>
 * A new instance of this class should be returned per each invokation
 * to the intercepting operation.
 *
 * @param <T> the generic type of the operation's return type
 * @since 1.0
 */
public interface InterceptingCallback<T> {

  /**
   * The operation's result
   *
   * @return the value returned by the intercepting operation
   * @throws Exception in case of error
   */
  T getResult() throws Exception; //TODO: MULE-8946 should be a MessagingException

  /**
   * Defaults to {@code true}
   *
   * @return Whether the intercepted chain should be executed or not.
   */
  default boolean shouldProcessNext() {
    return true;
  }

  /**
   * This method is invoked when the intercepted chain is
   * successfully executed.
   * <p>
   * Notice that this method will not be invoked if any of
   * the following conditions are met:
   * <p>
   * <ul>
   * <li>{@link #shouldProcessNext()} returns {@code false}</li>
   * <li>The intercepted chain failed</li>
   * </ul>
   *
   * @param resultMessage the message returned by the intercepted chain
   */
  default void onSuccess(MuleMessage resultMessage) {}

  /**
   * Invoked when the intercepted chain fails to be processed
   *
   * @param exception the exception thrown
   */
  default void onException(Exception exception) {}

  /**
   * Always invoked, regardless of the intercepted chain failing,
   * being successful or even non existing. This method is ideal
   * for performing clean-up task, freeing resources or any other
   * activity which should always happen no matter the result
   */
  default void onComplete() {

  }
}
