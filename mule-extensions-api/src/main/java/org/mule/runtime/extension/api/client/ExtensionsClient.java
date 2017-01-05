/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.util.concurrent.CompletableFuture;

/**
 * The {@link ExtensionsClient} is a simple common interface for executing extension operations programmatically without the
 * need of much manual coding and allowing to scale quickly all of this without a the need of compile dependency to
 * the executed extension.
 * <p>
 * This API is simple and easy to use, the user just needs to define the extension and the corresponding operation
 * that want's to invoke and pass a set of named parameters that will be attached when the operation gets executed. This implies
 * that the user should know the parameter names and types to create them using an {@link OperationParameters} instance. The idea
 * is that different extensions can provide different {@link OperationParameters} implementations for key operations.
 * <p>
 * This client lets the user reference complex configurations defined in the application and also has the capability to resolve
 * expressions.
 * <p>
 * Note that this client will be reachable through the mule registry and you will be able to inject it in any class with lifecycle.
 * <p>
 * An usage example for an operation with this signature {@code public String getName(@UseConfig config, int account)} could be:
 * <pre>
 * {@code
 * public class UsingExtensionsClient {
 *  @Inject ExtensionsClient client;
 *  ...
 *  public void executeWithClient() {
 *    OperationParameters parameters = DefaultOperationParameters.builder().configName("conf").addParameter("account", 12).build();
 *    Result<String, Attributes> result = client.execute(CustomExtension.class, "getName", parameters);
 *    if (result.getOutput().equals("DeveloperAccount"))
 *    ...
 *  }
 * }
 * }
 * </pre>
 *
 * @since 1.0
 */
public interface ExtensionsClient {

  /**
   * Executes an operation asynchronously by returning a {@link CompletableFuture} instance that will complete into a
   * {@link Result} with the corresponding payload and attributes after the operation execution finished.
   * <p>
   * This is the recommended method to use when the executed operation is non-blocking.
   * <p>
   * If the executed operation is not asynchronous in nature, the client might choose to actually execute in a synchronous manner.
   *
   * @param extension  the name of the extension that contains the operation to be executed.
   * @param operation  the name of the operation to be executed.
   * @param parameters an {@link OperationParameters} instance with all the parameters required to execute the operation.
   * @return a {@link CompletableFuture} instance that completes into a {@link Result} with the payload content and
   * the corresponding attributes.
   */
  <T, A extends Attributes> CompletableFuture<Result<T, A>> executeAsync(String extension, String operation,
                                                                         OperationParameters parameters);

  /**
   * Executes an operation synchronously and returns a {@link Result} with the operation's output and attributes if available.
   * <p>
   * Take in mind that if the executed operation is asynchronous in nature, this method will automatically wait for it to
   * complete before returning the value
   *
   * @param extension  the name of the extension that contains the operation to be executed.
   * @param operation  the name of the operation to be executed.
   * @param parameters an {@link OperationParameters} instance with all the parameters required to execute the operation.
   * @return a {@link Result} instance with the payload content and the corresponding attributes after the operation execution.
   * @throws MuleException if any error occurred while executing the operation.
   */
  <T, A extends Attributes> Result<T, A> execute(String extension, String operation, OperationParameters parameters)
      throws MuleException;
}
