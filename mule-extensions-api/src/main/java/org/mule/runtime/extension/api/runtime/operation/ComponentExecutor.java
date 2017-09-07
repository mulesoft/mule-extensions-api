/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;

import org.reactivestreams.Publisher;

/**
 * A facade interface which hides the details of how an
 * operation is actually executed. It aims to decouple
 * the abstract introspection model that the extension's
 * API proposes from the implementation details of the
 * underlying environment.
 *
 * @since 1.0
 */
public interface ComponentExecutor<T extends ComponentModel> {

  /**
   * Executes the owning operation using the given {@code executionContext}.
   * It returns a future to allow implementations on top of non-blocking execution engines.
   * This doesn't mean that it has to be executed in a non-blocking manner. Synchronous environments
   * can always return an immediate future.
   *
   * @param executionContext a {@link ExecutionContext} with information about the execution
   * @return the operations return value
   */
  Publisher<Object> execute(ExecutionContext<T> executionContext);
}
