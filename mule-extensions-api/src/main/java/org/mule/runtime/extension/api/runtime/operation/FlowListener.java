/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.message.Message;

import java.util.function.Consumer;

/**
 * Allows to execute custom logic when the flow on which an operation is being executed finishes.
 * <p>
 * Operation can declare an argument of this type and the runtime will automatically inject an implementation
 * which the operation can use.
 * <p>
 * An example use case is an operation which needs to await for the owning flow to finish in order to execute some clean-up,
 * or wants to know the final outcome in order to log it or audit it.
 * <p>
 * For example, let's see a very simple logging case
 * <p>
 * <pre>
 *
 *  public void listeningOperation(FlowListener listener) {
 *    listener.onSuccess(message -> LOGGER.debug("Response obtained", message.getPayload().getValue()));
 *    listener.onError(exception -> LOGGER.debug("Flow failed", exception));
 *    listener.onComplete(() -> doCleanUp());
 *    }
 *  }
 * </pre>
 *
 * Instances are not reusable and should not be cached. Instances are also not thread-safe. No instance should be used
 * in a thread different from the one executing the operation.
 *
 * @since 1.0
 */
public interface FlowListener {

  /**
   * Executes the given {@code handler} when the flow that owns the listening operation finishes.
   *
   * If the method is invoked several times on the same instance, the last handler wins and the prior ones get
   * discarded.
   *
   * The {@code handler} should be written in a way in which it doesn't fail. If it does fail, the runtime will
   * log and discard the error. Be a good citizen, do not put the runtime into that situation.
   * @param handler a {@link Message} {@link Consumer}
   */
  void onSuccess(Consumer<Message> handler);

  /**
   * Executes the given {@code handler} when the flow that owns the listening operation fails.
   *
   * If the method is invoked several times on the same instance, the last handler wins and the prior ones get
   * discarded.
   *
   * The {@code handler} should be written in a way in which it doesn't fail. If it does fail, the runtime will
   * log and discard the error. Be a good citizen, do not put the runtime into that situation.
   * @param handler an {@link Exception} {@link Consumer}
   */
  void onError(Consumer<Exception> handler);

  /**
   * Executes the given {@code handler} when the flow that owns the listening operation is completed, even because
   * it was successful or failed. Since this method is equivalent to a java {@code finally} block, it is ideal for
   * performing cleanup and resource deallocation tasks.
   *
   * The {@code handler} should be written in a way in which it doesn't fail. If it does fail, the runtime will
   * log and discard the error. Be a good citizen, do not put the runtime into that situation.
   * 
   * @param handler
   */
  void onComplete(Runnable handler);

}
