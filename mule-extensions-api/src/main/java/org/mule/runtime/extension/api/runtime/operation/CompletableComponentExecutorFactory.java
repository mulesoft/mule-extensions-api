/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;

import java.util.Map;

/**
 * Creates {@link CompletableComponentExecutor} instances
 *
 * @since 1.3.0
 */
public interface CompletableComponentExecutorFactory<T extends ComponentModel> {

  /**
   * Creates a new {@link CompletableComponentExecutor}
   *
   * @param componentModel the model of the component to be executed
   * @param parameters     parameters for initializing the executor
   * @return a new {@link CompletableComponentExecutor}
   */
  CompletableComponentExecutor<T> createExecutor(T componentModel, Map<String, Object> parameters);
}
