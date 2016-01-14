/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.introspection.declaration.fluent.OperationExecutorFactory;
import org.mule.extension.api.runtime.OperationExecutor;

/**
 * A definition of an operation in a {@link ExtensionModel}
 *
 * @since 1.0
 */
public interface OperationModel extends Described, EnrichableModel, InterceptableModel, ParametrizedModel, ExceptionEnrichableModel
{

    /**
     * Provides a {@link OperationExecutor} for this operation.
     *
     * @return a {@link OperationExecutor}
     */
    OperationExecutorFactory getExecutor();

    /**
     * Returns the operation's return type
     *
     * @return a {@link DataType}
     */
    DataType getReturnType();

}
