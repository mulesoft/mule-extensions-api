/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * The {@link ExtensionsClient} is a simple interface for executing extension operations programmatically.
 * <p>
 * The operation to execute is referenced through its name and the name of the extension in which it's defined. Notice that the
 * extension is referenced by name and not GAV, which means that the extension is assumed to be present and activated on the
 * current execution context.
 * <p>
 * Once the operation is located, the operation execution is parameterized by not only providing parameter values, but also config
 * reference, reconnection and streaming strategies, etc.
 * <p>
 * Note that this client will be reachable through the mule registry, and you will be able to inject it in any class with
 * lifecycle.
 * <p>
 * A usage example for an operation with this signature {@code public String getName(@UseConfig config, int account)} could be:
 *
 * <pre>
 * {@code
 * public class UsingExtensionsClient {
 *  &#64;Inject ExtensionsClient client;
 *  ...
 *  public void executeWithClient() {
 *    client.&lt;String, Object&gt;executeAsync("myExtensionName", "getName", params ->
 *  		params.withConfigRef("conf").withParameter("account", 12)
 *  	).whenComplete((result, e) -> {
 *  		if (e != null) {
 *  			logError(e);
 *            } else {
 *  			processResult(result);
 *      }
 *   });
 * }
 * </pre>
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface ExtensionsClient {


  /**
   * Executes an operation asynchronously by returning a {@link CompletableFuture} instance that will complete into a
   * {@link Result} with the corresponding payload and attributes after the operation execution finished.
   * <p>
   * If the executed operation is not asynchronous in nature, the client might choose to actually execute in a synchronous manner.
   *
   * @param extension  the name of the extension that contains the operation to be executed.
   * @param operation  the name of the operation to be executed.
   * @param parameters
   * @param <T>        The generic type of the result's payload
   * @param <A>        The generic type of the result's attribute
   * @return a {@link CompletableFuture} instance that completes into a {@link Result} with the payload content and the
   *         corresponding attributes.
   * @since 4.5.0
   */
  @MinMuleVersion("4.5.0")
  <T, A> CompletableFuture<Result<T, A>> executeAsync(String extension,
                                                      String operation,
                                                      Consumer<OperationParameterizer> parameters);

  /**
   * Executes an operation asynchronously by returning a {@link CompletableFuture} instance that will complete into a
   * {@link Result} with the corresponding payload and attributes after the operation execution finished.
   * <p>
   * If the executed operation is not asynchronous in nature, the client might choose to actually execute in a synchronous manner.
   *
   * @param extension  the name of the extension that contains the operation to be executed.
   * @param operation  the name of the operation to be executed.
   * @param parameters an {@link OperationParameters} instance with all the parameters required to execute the operation.
   * @return a {@link CompletableFuture} instance that completes into a {@link Result} with the payload content and the
   *         corresponding attributes.
   * @deprecated since 4.5.0. Use {@link #executeAsync(String, String, Consumer)} instead
   */
  @Deprecated
  <T, A> CompletableFuture<Result<T, A>> executeAsync(String extension, String operation,
                                                      OperationParameters parameters);

  /**
   * Executes an operation synchronously and returns a {@link Result} with the operation's output and attributes if available.
   * <p>
   * Take in mind that if the executed operation is asynchronous in nature, this method will automatically wait for it to complete
   * before returning the value
   *
   * @param extension  the name of the extension that contains the operation to be executed.
   * @param operation  the name of the operation to be executed.
   * @param parameters an {@link OperationParameters} instance with all the parameters required to execute the operation.
   * @return a {@link Result} instance with the payload content and the corresponding attributes after the operation execution.
   * @throws MuleException if any error occurred while executing the operation.
   * @deprecated since 4.5.0. Use {@link #executeAsync(String, String, Consumer)} instead
   */
  @Deprecated
  <T, A> Result<T, A> execute(String extension, String operation, OperationParameters parameters)
      throws MuleException;
}
