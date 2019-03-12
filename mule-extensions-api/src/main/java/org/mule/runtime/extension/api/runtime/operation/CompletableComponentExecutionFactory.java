/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;

import java.util.Map;

public interface CompletableComponentExecutionFactory <T extends ComponentModel> {

  /**
   * Creates a new {@link CompletableComponentExecutor}
   *
   * @param componentModel the model of the component to be executed
   * @param parameters     parameters for initializing the executor
   * @return a new {@link CompletableComponentExecutor}
   */
  CompletableComponentExecutor<T> createExecutor(T componentModel, Map<String, Object> parameters);

}
