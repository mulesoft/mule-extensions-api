/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;

/**
 * Callback used by {@link Source sources} in order tell the runtime that they will process the flow's response in an asynchronous
 * manner (from now on, async sources).
 * <p>
 * Main uses cases for async sources are (but not limited to):
 * <ul>
 * <li>Response operation is non-blocking way</li>
 * <li>Response operation is blocking, but it's asynchronously executed in a thread other than the one which initiates it (most
 * typically, the thread executing a method annotated with {@link OnSuccess} or{@link OnError}</li>
 * <li>The source wants to perform asynchronous auditing for which it needs to consume a response stream in a separate thread</li>
 * </ul>
 * <p>
 * In those use cases, the runtime needs to know that the response will be asynchronous because otherwise it will generate a race
 * condition between the thread emitting the response and the runtime itself which is trying to free up resources associated with
 * the message being responded to.
 * <p>
 * In those use cases, the methods annotated with {@link OnSuccess} or {@link OnError} can have an argument of this type. That's
 * enough to signal the runtime that the source is an async one. The runtime will not finish the associated event until either
 * {@link #success()} or {@link #error(Throwable)} methods are invoked. Notice this is a very strong piece of the contract. A
 * source which requests a {@link SourceCompletionCallback} but then doesn't properly notifies it is one likely to eventually
 * freeze the entire runtime!.
 * <p>
 * Let's see a quick example:
 * <p>
 * 
 * <pre>
 *  &#64;OnSuccess
 *  public void onSuccess(@Content String response, SourceCompletionCallback callback) {
 *    asyncResponder.sendResponse(response, new ResponderCallback() {
 *        void onSuccess() {
 *          callback.success();
 *        }
 *
 *        void onFailure(Throwable t) {
 *         callback.error(t);
 *       }
 *      }
 *    }
 * </pre>
 * <p>
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.runtime.source.SourceCompletionCallback} instead.
 */
@NoImplement
@Deprecated
public interface SourceCompletionCallback extends org.mule.sdk.api.runtime.source.SourceCompletionCallback {

}
