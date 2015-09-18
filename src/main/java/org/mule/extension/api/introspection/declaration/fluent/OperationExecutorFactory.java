/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.runtime.OperationExecutor;

/**
 * Creates {@link OperationExecutor} instances
 *
 * @since 1.0
 */
public interface OperationExecutorFactory
{

    /**
     * Creates a new {@link OperationExecutor}
     *
     * @return a new {@link OperationExecutor}
     */
    OperationExecutor createExecutor();
}
