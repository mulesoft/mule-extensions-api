/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.operation;

import org.mule.runtime.extension.api.introspection.RuntimeComponentModel;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutor;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutorFactory;

/**
 * A specialization of the {@link OperationModel} interface which adds
 * behavioural components that are relevant to the extension's functioning
 * when in runtime.
 *
 * @see OperationModel
 * @since 1.0
 */
public interface RuntimeOperationModel extends RuntimeComponentModel, OperationModel
{

    /**
     * Provides a {@link OperationExecutor} for this operation.
     *
     * @return a {@link OperationExecutor}
     */
    OperationExecutorFactory getExecutor();
}
