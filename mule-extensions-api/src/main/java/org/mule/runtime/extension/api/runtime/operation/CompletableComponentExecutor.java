/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.component.execution.CompletableCallback;
import org.mule.runtime.api.meta.model.ComponentModel;

/**
 * A facade interface which hides the details of how an operation is actually executed. It aims to decouple the abstract
 * introspection model that the extensions API proposes from the implementation details of the underlying environment.
 *
 * @since 1.3.0
 */
public interface CompletableComponentExecutor<M extends ComponentModel> {

  /**
   * Executes the operation using the given {@code executionContext}. The outcome will be informed by calling the appropriate
   * method in the {@code callback}. Although this pattern is optimized for non blocking execution, notice that using this
   * interface doesn't guarantee that non blocking execution will take place. The callback can always be completed synchronously.
   *
   * @param executionContext a {@link ExecutionContext} with information about the execution
   * @param callback         the {@link ExecutorCallback} to notify the operation's result
   */
  void execute(ExecutionContext<M> executionContext, ExecutorCallback callback);

  /**
   * Callback to notify the operation's result.
   */
  interface ExecutorCallback extends CompletableCallback<Object> {

    /**
     * Invoked when the operation completed successfully. If the operation is void, a {@code null} value should be passed
     *
     * @param value the operation's result
     */
    @Override
    void complete(Object value);

    /**
     * This method is invoked when the operation failed to execute.
     *
     * @param e the exception found
     */
    @Override
    void error(Throwable e);
  }

}
