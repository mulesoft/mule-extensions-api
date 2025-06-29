/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.function;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.el.ExpressionFunction;
import org.mule.runtime.api.meta.model.function.FunctionModel;

/**
 * Factory for creating {@link FunctionExecutor}s based on a given {@link FunctionModel}
 *
 * @since 1.10.0
 */
@NoImplement
public interface FunctionExecutorFactory {

  /**
   * Creates a new {@link ExpressionFunction} based on a given {@link FunctionModel}
   *
   * @param functionModel the model of the function to be executed
   * @return a new {@link ExpressionFunction}
   */
  FunctionExecutor createExecutor(FunctionModel functionModel,
                                  FunctionParameterDefaultValueResolverFactory defaultResolverFactory);
}
