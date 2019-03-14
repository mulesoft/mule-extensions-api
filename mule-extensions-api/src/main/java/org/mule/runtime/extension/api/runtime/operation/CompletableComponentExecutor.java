/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;

public interface CompletableComponentExecutor<M extends ComponentModel> {

  /**
   * Executes the owning operation using the given {@code executionContext}.
   * It returns a future to allow implementations on top of non-blocking execution engines.
   * This doesn't mean that it has to be executed in a non-blocking manner. Synchronous environments
   * can always return an immediate future.
   *
   * @param executionContext a {@link ExecutionContext} with information about the execution
   * @return the operations return value
   */
  void execute(ExecutionContext<M> executionContext, ExecutorCallback callback);

  interface ExecutorCallback {

    void complete(Object value);

    /**
     * This method is not be invoked when the operation failed to execute.
     *
     * @param e the exception found
     */
    void error(Throwable e);
  }

}
