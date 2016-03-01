/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.runtime.OperationExecutorFactory;
import org.mule.metadata.api.model.MetadataType;

import java.util.Optional;

/**
 * A declaration object for a {@link OperationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link OperationModel}
 *
 * @since 1.0
 */
public class OperationDeclaration extends ParameterizedInterceptableDeclaration<OperationDeclaration>
{

    private OperationExecutorFactory executorFactory;
    private MetadataType returnType;
    private Optional<ExceptionEnricherFactory> exceptionEnricherFactory;

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

    public MetadataType getReturnType()
    {
        return returnType;
    }

    public void setReturnType(MetadataType returnType)
    {
        this.returnType = returnType;
    }

    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    public void setExceptionEnricherFactory(Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }
}
