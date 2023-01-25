/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.source;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Callback used by {@link ExtensionsClient} to notify of new {@link Result results} produced by a source created through the
 * {@link ExtensionsClient#createSource(String, String, Consumer, Consumer)} method.
 * <p>
 * Clients will obtain the produced value by calling the {@link #getResult()} method.
 * <p>
 * After processing the result, clients <b>MUST</b> invoke either {@link #completeWithSuccess(Consumer)} or
 * {@link #completeWithError(Throwable, Consumer)}, so that the source can finish the processing of each individual result. Refer
 * to those method's javadocs for further details on their semantics.
 * <p>
 * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
 *
 * @param <T> the generic type of the result output value
 * @param <A> the generic type of the result attributes value
 * @since 1.6.0
 */
@Experimental
@MinMuleVersion("4.6.0")
public interface SourceResultHandler<T, A> {

  /**
   * @return a {@link Result} produced by the message source
   */
  Result<T, A> getResult();

  /**
   * Executes the source's completion callback. This method <b>MUST</b> be invoked when the client has successfully processed the
   * {@link #getResult()}, even if the {@link SourceModel} doesn't explicitly define a success {@link SourceCallbackModel}.
   * <p>
   * The actual effect of calling this method will depend on the source (some will emit a response back to the client, some will
   * update internal state, etc.). The action is however asynchronous, for which the method returns a {@link CompletableFuture},
   * which will either complete with a void value or an Exception.
   * <p>
   * Unlike what happens with a source created inside a Mule app, should this method fail (either synchronously upon invocation or
   * asynchronously with an exceptional completion of the return future), the {@link #completeWithError(Throwable, Consumer)}
   * method <b>will not</b> be called automatically.
   * <p>
   * Finally, this method should only be called <b>at most once</b> per instance and should be mutually exclusive with
   * {@link #completeWithError(Throwable, Consumer)}, which means that if one is called, the other one shouldn't.
   *
   * @param successCallbackParameters parameterizes the source's success callback.
   * @return a {@link CompletableFuture}
   */
  CompletableFuture<Void> completeWithSuccess(Consumer<SourceCallbackParameterizer> successCallbackParameters);

  /**
   * Executes the source's error callback. This method <b>MUST</b> be invoked when the client has encountered an Exception while
   * processing the {@link #getResult()} which should be notified back to the source so that it considers the Result as a failed
   * event. This must be done even if the {@link SourceModel} doesn't explicitly define an error {@link SourceCallbackModel}.
   * <p>
   * The actual effect of calling this method will depend on the source (some will emit a response back to the client, some will
   * update internal state, etc.). The action is however asynchronous, for which the method returns a {@link CompletableFuture},
   * which will either complete with a void value or an Exception.
   * <p>
   * Finally, this method should only be called <b>at most once</b> per instance and should be mutually exclusive with
   * {@link #completeWithSuccess(Consumer)}, which means that if one is called, the other one shouldn't.
   *
   * 
   * @param exception               The exception found
   * @param errorCallbackParameters parameterizes the source's error callback.
   * @return a {@link CompletableFuture}
   */
  CompletableFuture<Void> completeWithError(Throwable exception,
                                            Consumer<SourceCallbackParameterizer> errorCallbackParameters);
}
