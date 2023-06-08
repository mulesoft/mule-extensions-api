/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.api.annotation.Experimental;
import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.client.source.SourceHandler;
import org.mule.runtime.extension.api.client.source.SourceParameterizer;
import org.mule.runtime.extension.api.client.source.SourceResultHandler;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
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
   * @param parameters consumers an {@link OperationParameterizer} used to configure the operation
   * @param <T>        The generic type of the result's payload
   * @param <A>        The generic type of the result's attribute
   * @return a {@link CompletableFuture} instance that completes into a {@link Result} with the payload content and the
   *         corresponding attributes.
   * @since 4.5.0
   */
  @MinMuleVersion("4.5.0")
  <T, A> CompletableFuture<Result<T, A>> execute(String extension,
                                                 String operation,
                                                 Consumer<OperationParameterizer> parameters);

  /**
   * Creates and initialises a {@link Source} using a given parameterization.
   * <p>
   * The generated messages will be passed back to the caller through a {@link Consumer} passed in the {@code callback} argument.
   * The provided {@link SourceResultHandler} has a pretty strict contract around how it should be consumed. Follow that contract
   * <b>thoroughly</b> in order to avoid resource leaks or protocol issues with the remote system.
   * <p>
   * The created source is returned in the form of a {@link SourceHandler}. The main purpose of it is to control the lifecycle of
   * the underlying source. When returned, the source is already is initialised (as defined in {@link Initialisable#initialise()})
   * but not started, which means it isn't producing any messages. The source will be started when {@link SourceHandler#start()}
   * is invoked. {@link SourceHandler#stop()} can be called for the source to stop producing messages, and
   * {@link SourceHandler#dispose()} when the source is no longer needed. Stopped sources can be restarted, but disposed sources
   * are not recoverable, they need to be created again through another call to this method.
   * <p>
   * When {{@code this} {@link ExtensionsClient} is disposed, all active {@link SourceHandler} will be stopped and disposed as
   * well.
   * <p>
   * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
   *
   * @param extension  the name of the extension in which the source is defined
   * @param sourceName the name of the source to be created (as it appears in the {@link ExtensionModel}
   * @param handler    a {@link Consumer} that will be invoked each time the source produces a new message, in the form of a
   *                   {@link SourceResultHandler}
   * @param parameters consumers an {@link OperationParameterizer} used to configure the source. This is for the source main
   *                   parameters, not it's callbacks
   * @param <T>        the generic type of the result output values produced by the source
   * @param <A>        the generic type of the result attribute values produced by the source
   * @return a {@link SourceHandler}
   * @see SourceHandler
   * @see SourceResultHandler
   * @see SourceParameterizer
   * @since 1.5.0
   */
  @MinMuleVersion("4.6.0")
  @Experimental
  <T, A> SourceHandler createSource(String extension,
                                    String sourceName,
                                    Consumer<SourceResultHandler<T, A>> handler,
                                    Consumer<SourceParameterizer> parameters);

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
   * @deprecated since 4.5.0. Use {@link #execute(String, String, Consumer)} instead
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
   * @deprecated since 4.5.0. Use {@link #execute(String, String, Consumer)} instead
   */
  @Deprecated
  <T, A> Result<T, A> execute(String extension, String operation, OperationParameters parameters)
      throws MuleException;
}
