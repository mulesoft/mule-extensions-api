/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.OperationExecutor;

/**
 * A {@link Descriptor} which allows configuring a {@link OperationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class OperationDescriptor extends HasParameters implements Descriptor, HasModelProperties<OperationDescriptor>, HasInterceptors<OperationDescriptor>
{

    private final OperationDeclaration operation;
    private final DeclarationDescriptor declaration;

    /**
     * Creates a new instance
     * @param operation the {@link OperationDeclaration} which will be defined through {@code this} {@link Descriptor}
     * @param declaration the {@link DeclarationDescriptor} which owns {@code this} one
     */
    OperationDescriptor(OperationDeclaration operation, DeclarationDescriptor declaration)
    {
        this.operation = operation;
        this.declaration = declaration;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@value this} descriptor
     */
    public OperationDescriptor describedAs(String description)
    {
        operation.setDescription(description);
        return this;
    }

    /**
     * @return a {@link WithParameters} which allows adding {@link ParameterDeclaration}
     * to this descriptor
     */
    public WithParameters with()
    {
        return new WithParameters(this, getRootDeclaration());
    }

    /**
     * Adds a {@link ParameterDeclaration}
     *
     * @param parameter a {@link ParameterDeclaration}
     */
    @Override
    protected void addParameter(ParameterDeclaration parameter)
    {
        operation.addParameter(parameter);
    }

    /**
     * @return the root {@link DeclarationDescriptor}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return declaration;
    }

    /**
     * Specifies the {@link OperationExecutorFactory} to be used
     * to create {@link OperationExecutor} instances.
     *
     * @param executorFactory a {@link OperationExecutorFactory}
     * @return {@value this} descriptor
     */
    public OperationDescriptor executorsCreatedBy(OperationExecutorFactory executorFactory)
    {
        operation.setExecutorFactory(executorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDescriptor withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        operation.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDescriptor withModelProperty(String key, Object value)
    {
        operation.addModelProperty(key, value);
        return this;
    }

    /**
     * @return the {@link OperationDeclaration} which is being defined by {@code this} {@link Descriptor}
     */
    public OperationDeclaration getDeclaration()
    {
        return operation;
    }
}
