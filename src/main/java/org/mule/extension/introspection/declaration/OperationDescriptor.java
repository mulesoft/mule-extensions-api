/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.runtime.OperationExecutor;

/**
 * A {@link Descriptor} which allows configuring a {@link OperationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public final class OperationDescriptor extends HasParameters implements Descriptor, HasCapabilities<OperationDescriptor>
{
    private final OperationDeclaration operation;
    private final DeclarationDescriptor declaration;

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

    OperationDescriptor(OperationDeclaration operation, DeclarationDescriptor declaration)
    {
        this.operation = operation;
        this.declaration = declaration;
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
     * Adds another operation to the root {@link DeclarationDescriptor}
     *
     * @param name the name of the operation
     * @return a new {@link OperationDescriptor}
     */
    public OperationDescriptor withOperation(String name)
    {
        return getRootDeclaration().withOperation(name);
    }

    /**
     * Adds another configuration to the root {@link DeclarationDescriptor}
     *
     * @param name the name of the configuration
     * @return a new {@link ConfigurationDescriptor}
     */
    public ConfigurationDescriptor withConfig(String name)
    {
        return getRootDeclaration().withConfig(name);
    }

    /**
     * Adds the given capability to the declaring operation
     *
     * @param capability a not {@code null} capability
     * @return {@value this} descriptor
     */
    @Override
    public OperationDescriptor withCapability(Object capability)
    {
        operation.addCapability(capability);
        return this;
    }
}
