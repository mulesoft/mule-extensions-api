/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.extension.api.introspection.operation.OperationModel;

/**
 * Creates {@link OperationExecutor} instances
 *
 * @since 1.0
 */
public interface OperationExecutorFactory {

  /**
   * Creates a new {@link OperationExecutor}
   *
   * @param operationModel the model of the operation to be executed
   * @return a new {@link OperationExecutor}
   */
  OperationExecutor createExecutor(OperationModel operationModel);
}
