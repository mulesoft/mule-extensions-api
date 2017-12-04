/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.util.function.Consumer;

/**
 * A {@link FlowExecutor} allows a given {@link OperationModel Operation} to execute a Flow
 * part of the operation's execution.
 * <p>
 * The execution of the Flow is completed either when all the components were invoked successfully or when an error
 * occurs in one processor and it is propagated to the {@link FlowExecutor}
 *
 * @since 1.0
 */
public interface FlowExecutor {

  /**
   * Executes the chain of components starting with the same input message that it's container scope received.
   * <p>
   * {@code onSuccess} callback will be invoked with the output {@link Result} of the last component
   * in the Flow if no error occurred after all the components were executed exactly once.
   * <p>
   * {@code onError} callback will be invoked with the exception propagated by the first failing component.
   *
   * @param onSuccess the callback to be executed when a successful execution is completed by the Flow
   * @param onError  the callback to be executed when an error occurs during the execution of the Flow
   */
  void process(Consumer<Result> onSuccess, Consumer<Throwable> onError);

  /**
   * Executes the chain of components starting with the given {@code payload} and {@code attributes} as {@link Message}.
   * <p>
   * {@code onSuccess} callback will be invoked with the output {@link Result} of the last component
   * in the Flow if no error occurred after all the components were executed exactly once.
   * <p>
   * {@code onError} callback will be invoked with the exception propagated by the first failing component.
   *
   * @param onSuccess the callback to be executed when a successful execution is completed by the Flow
   * @param onError  the callback to be executed when an error occurs during the execution of the Flow
   */
  void process(Object payload, Object attributes, Consumer<Result> onSuccess, Consumer<Throwable> onError);

}
