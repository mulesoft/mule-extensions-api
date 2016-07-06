/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutorFactory;

/**
 * A declaration object for a {@link OperationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link OperationModel}
 *
 * @since 1.0
 */
public class OperationDeclaration extends ComponentDeclaration<OperationDeclaration>
{

    private OperationExecutorFactory executorFactory;

    /**
     * {@inheritDoc}
     */
    OperationDeclaration(String name)
    {
        super(name);
    }

    public OperationExecutorFactory getExecutorFactory()
    {
        return executorFactory;
    }

    public void setExecutorFactory(OperationExecutorFactory executorFactory)
    {
        this.executorFactory = executorFactory;
    }

}
