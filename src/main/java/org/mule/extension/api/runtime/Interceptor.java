/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

/**
 * Taps into different phases of the execution of an extension's operation
 * allowing to take different actions depending on the phase.
 * <p/>
 * One instance of each implementation will be created per {@link ConfigurationInstance}.
 * Those instances can have lifecycle, can be stateful and can use {@code JSR-330}
 * annotations for dependency injection. However, they <b>MUST</b> be thread-safe and
 * reusable.
 *
 * @since 1.0
 */
public interface Interceptor
{

    /**
     * Executes before the operation is executed.
     * <p/>
     * If this method fails, the exception will be  bubbled up right away.
     * No other method in this interceptor will be executed (not even the {@link #after(OperationContext, Object)},
     * nor any other of the interceptors in line will be executed either. Because of this, no implementation
     * should rely on the execution of any other method in this or other interceptor
     *
     * @param operationContext the {@link OperationContext} for the operation to be executed
     * @throws Exception in case of error
     */
    default void before(OperationContext operationContext) throws Exception
    {

    }

    /**
     * Executes when an operation was successfully executed.
     * <p/>
     * Implementations of this method should not fail. If they do throw any exception, it will
     * be logged and ignored.
     * <p/>
     * The {@link #after(OperationContext, Object)} method is guaranteed to be executed regardless
     * of this method's outcome in {@code this} or other involved instances
     *
     * @param operationContext the {@link OperationContext} that was used to execute the operation
     * @param result           the result of the operation. Can be {@code null} if the operation itself returned that.
     */
    default void onSuccess(OperationContext operationContext, Object result)
    {
    }

    /**
     * Executes when the execution of an operation threw exception.
     * <p/>
     * This method returns {@link Exception} to allow implementations to decorate, enrich or even replace
     * the exception that will be bubbled up. Implementations however are not obligated to do such thing
     * in which case they should return the same {@code exception} supplied. Notice however that:
     * <li>
     * <ul>The next interceptor in line will receive the exception returned by this method, which might
     * not be the same one supplied in this invokation.</ul>
     * <ul>There's no guarantee that the exception returned by this method will be the one bubbled up, since
     * the next interceptor in line might also change it.</ul>
     * </li>
     * </ul>
     * <p/>
     * Some interceptors might deal with the concept of retries. For example, an operation failing because of
     * stale connection might attempt to reconnect and try again. For such interceptors, a {@code retryRequest}
     * is provided so that a retry can be requested. The runtime is not obligated to grant such request and might
     * decide to ignore it. Notice that the {@link #onError(OperationContext, RetryRequest, Exception)} and
     * {@link #after(OperationContext, Object)} method in all the other interceptors in line will be executed before the
     * runtime decides to ignore/grant the retry request. If the petition is granted, then not only the operation will be
     * re-executed. The whole interceptor chain (including the {@link #before(OperationContext)} will also be executed again.
     * <p/>
     * Implementations of this method should not fail. If they do throw any exception, it will
     * be logged and ignored.
     * <p/>
     * The {@link #after(OperationContext, Object)} method is guaranteed to be executed regardless
     * of this method's outcome in {@code this} or other involved instances
     *
     * @param operationContext the {@link OperationContext} that was used to execute the operation
     * @param retryRequest     a {@link RetryRequest} in case that the operation should be retried
     * @param exception        the {@link Exception} that was thrown by the failing operation
     * @return the {@link Exception} that should be propagated forward
     */
    default Exception onError(OperationContext operationContext, RetryRequest retryRequest, Exception exception)
    {
        return exception;
    }

    /**
     * Executes after the execution of an operation is finished, regardless of it being successful or not.
     * <p/>
     * In practical terms, it executes after the {@link #onSuccess(OperationContext, Object)} or
     * {@link #onError(OperationContext, RetryRequest, Exception)} but it doesn't execute if
     * {@link #before(OperationContext)} threw exception.
     * <p/>
     * The {@code result} argument holds the return value of the operation. Because this method is invoked
     * even if the operation failed, then the {@code result} will be a {@code null} in such a case. However,
     * notice that testing {@code result} for being {@code null} is not an indicator of the operation having
     * failed or not, since the operation might have successfully returned {@code null}. This method should
     * be used for actions that should take place &quot;no matter what&quot;. Actions that should depend on
     * the operation's outcome are to be implemented using {@link #onSuccess(OperationContext, Object)} or
     * {@link #onError(OperationContext, RetryRequest, Exception)}
     *
     * @param operationContext the {@link OperationContext} that was used to execute the operation
     * @param result           the result of the operation. Can be {@code null} if the operation itself returned that or failed.
     */
    default void after(OperationContext operationContext, Object result)
    {

    }

}
