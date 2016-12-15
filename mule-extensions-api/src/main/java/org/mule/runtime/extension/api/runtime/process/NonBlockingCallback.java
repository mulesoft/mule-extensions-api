/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.extension.api.runtime.operation.Result;

/**
 * This callback is how non blocking operations notify their outcome.
 * <p>
 * In order to implement a non blocking operation, the method needs to:
 * <p>
 * <ul>
 * <li>Have a void return type</li>
 * <li>Have an argument of this type, with its generics correctly provided</li>
 * </ul>
 * <p>
 * The operation's output will be derived from the generics, so even though the method
 * is void, the runtime will consider the operation to return values of the generic
 * types.
 * <p>
 * When the non blocking operation has finished, it has to notify the result either by
 * invoking the {@link #success(Result)} or {@link #error(Exception)} methods.
 * Only then will the operation be considered as completed and the next processor in the
 * pipeline will be executed.
 * <p>
 * For example, let's see a very simple non blocking http request
 * <p>
 * <pre>
 *
 *  public void request(String path, @Content Object content, NonBlockingCallback<InputStream, HttpAttributes> callback) {
 *    httpClient.requestNonBlocking(path, content, new HttpClientCallback() {
 *      void onSuccess(HttpResponse response) {
 *        callback.onSuccess(Result.<InputStream, HttpAttributes>.builder()
 *          .output(response.getBody())
 *          .attributes(toAttributes(response))
 *          .build());
 *      }
 *
 *      void onFailure(Exception e) {
 *        callback.onException(e);
 *       }
 *    }
 *  }
 * </pre>
 * <p>
 * If the operation is void, the {@link Void} type can be used in the generics.
 *
 * @param <T> The generic type of the operation's output value
 * @param <A> The generic type of the operation's output attributes
 */
public interface NonBlockingCallback<T, A extends Attributes> {

  /**
   * This method is to be invoked with the operation's result.
   * The value itself has to be provided with a {@link Result}
   * instance.
   *
   * @param result the operation's result
   */
  void success(Result<T, A> result);

  /**
   * This method is not be invoked when the operation failed to execute.
   *
   * @param e the exception found
   */
  void error(Exception e);

}
