/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

import org.mule.extension.introspection.Configuration;
import org.mule.extension.runtime.OperationExecutor;

/**
 * A factory object for creating {@link OperationExecutor} instances
 *
 * @since 1.0
 */
public interface OperationExecutorFactory
{

    /**
     * Provides a {@link OperationExecutor} that will act on the
     * given {@code configurationInstance}
     *
     * @param configurationInstance an object to act as a concrete configuration instance. This is not a {@link Configuration}
     *                              but the actual object configuring the implementation
     * @param <C>                   the type of the {@code configurationInstance}
     * @return a {@link OperationExecutor}
     */
    <C> OperationExecutor getExecutor(C configurationInstance);
}
