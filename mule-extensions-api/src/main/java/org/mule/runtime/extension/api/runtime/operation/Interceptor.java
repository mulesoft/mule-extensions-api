/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.extension.api.runtime.config.ConfigurationInstance;

/**
 * Taps into different phases of the execution of an extension's operation allowing to take different actions depending on the
 * phase.
 * <p/>
 * One instance of each implementation will be created per {@link ConfigurationInstance}. Those instances can have lifecycle, can
 * be stateful and can use {@code JSR-330} annotations for dependency injection. However, they <b>MUST</b> be thread-safe and
 * reusable.
 *
 * @since 1.0
 */
public interface Interceptor<T extends ComponentModel> {

  /**
   * Executes before the operation is executed.
   * <p/>
   * If this method fails, the exception will be bubbled up right away. No other method in this interceptor will be executed (not
   * even the {@link #after(ExecutionContext, Object)}, nor any other of the interceptors in line will be executed either. Because
   * of this, no implementation should rely on the execution of any other method in this or other interceptor
   *
   * @param executionContext the {@link ExecutionContext} for the operation to be executed
   * @throws Exception in case of error
   */
  default void before(ExecutionContext<T> executionContext) throws Exception {

  }

  /**
   * Executes when an operation was successfully executed.
   * <p/>
   * Implementations of this method should not fail. If they do throw any exception, it will be logged and ignored.
   * <p/>
   * The {@link #after(ExecutionContext, Object)} method is guaranteed to be executed regardless of this method's outcome in
   * {@code this} or other involved instances
   *
   * @param executionContext the {@link ExecutionContext} that was used to execute the operation
   * @param result           the result of the operation. Can be {@code null} if the operation itself returned that.
   */
  default void onSuccess(ExecutionContext<T> executionContext, Object result) {}

  /**
   * Executes when the execution of an operation threw exception.
   * <p/>
   * This method returns {@link Exception} to allow implementations to decorate, enrich or even replace the exception that will be
   * bubbled up. Implementations however are not obligated to do such thing in which case they should return the same
   * {@code exception} supplied. Notice however that:
   * <li>
   * <ul>
   * The next interceptor in line will receive the exception returned by this method, which might not be the same one supplied in
   * this invokation.
   * </ul>
   * <ul>
   * There's no guarantee that the exception returned by this method will be the one bubbled up, since the next interceptor in
   * line might also change it.
   * </ul>
   * </li>
   * </ul>
   * <p/>
   * Implementations of this method should not fail. If they do throw any exception, it will be logged and ignored.
   * <p/>
   * The {@link #after(ExecutionContext, Object)} method is guaranteed to be executed regardless of this method's outcome in
   * {@code this} or other involved instances
   *
   * @param executionContext the {@link ExecutionContext} that was used to execute the operation
   * @param exception        the {@link Exception} that was thrown by the failing operation
   * @return the {@link Exception} that should be propagated forward
   */
  default Throwable onError(ExecutionContext<T> executionContext, Throwable exception) {
    return exception;
  }

  /**
   * Executes after the execution of an operation is finished, regardless of it being successful or not.
   * <p/>
   * In practical terms, it executes after the {@link #onSuccess(ExecutionContext, Object)} or
   * {@link #onError(ExecutionContext, Throwable)} but it doesn't execute if {@link #before(ExecutionContext)} threw exception.
   * <p/>
   * The {@code result} argument holds the return value of the operation. Because this method is invoked even if the operation
   * failed, then the {@code result} will be a {@code null} in such a case. However, notice that testing {@code result} for being
   * {@code null} is not an indicator of the operation having failed or not, since the operation might have successfully returned
   * {@code null}. This method should be used for actions that should take place &quot;no matter what&quot;. Actions that should
   * depend on the operation's outcome are to be implemented using {@link #onSuccess(ExecutionContext, Object)} or
   * {@link #onError(ExecutionContext, Throwable)}
   *
   * @param executionContext the {@link ExecutionContext} that was used to execute the operation
   * @param result           the result of the operation. Can be {@code null} if the operation itself returned that or failed.
   */
  default void after(ExecutionContext<T> executionContext, Object result) {

  }

}
