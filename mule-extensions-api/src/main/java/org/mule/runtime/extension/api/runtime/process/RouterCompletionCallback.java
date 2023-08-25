/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.route.Route;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * This callback is how a Router receiving {@link Route}s notify their outcome.
 * <p>
 * In order to implement a Router (that is, an operation that receives one or more {@link Route}s), the method needs to:
 * <p>
 * <ul>
 * <li>Have a void return type</li>
 * <li>Have at least one argument of {@link Route} type</li>
 * <li>Have an argument of {@link RouterCompletionCallback} type</li>
 * </ul>
 * <p>
 * When the execution of the Router has finished, it has to notify the {@link Result} either by invoking the
 * {@link #success(Result)} or {@link #error(Throwable)} methods. Only then will the execution of the Router be considered as
 * completed and the next processor in the pipeline will be executed. <b>If the {@link #success(Result)} or
 * {@link #error(Throwable)} methods are invoked before any of the nested {@link Route}s is completed, the {@link Result} of the
 * nested execution will be lost and never propagated.</b>
 *
 * For example, a Router can be declared as:
 * <p>
 * 
 * <pre>
 * 
 * public void twoRoutesRouter(WhenRoute when, @Optional OtherwiseRoute other, RouterCompletionCallback callback) {
 *   if (when.shouldExecute()) {
 *     when.getChain().process(routeResult -> callback.success(routeResult), (e, r) -> callback.error(e));
 *   } else if (other != null && other.shouldExecute()) {
 *     other.getChain().process(callback::success, (e, r) -> callback.error(e));
 *   } else {
 *     callback.error(new IllegalArgumentException("No route could be executed"));
 *   }
 * }
 * </pre>
 * <p>
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface RouterCompletionCallback extends CompletionCallback<Object, Object> {


}
