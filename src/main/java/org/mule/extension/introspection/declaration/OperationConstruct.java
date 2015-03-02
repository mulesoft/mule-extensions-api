/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.OperationImplementation;

/**
 * A {@link Construct} which allows configuring a {@link OperationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public final class OperationConstruct extends HasParameters implements Construct, HasCapabilities<OperationConstruct>
{
    private final OperationDeclaration operation;
    private final DeclarationConstruct declaration;

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@value this} construct
     */
    public OperationConstruct describedAs(String description)
    {
        operation.setDescription(description);
        return this;
    }

    OperationConstruct(OperationDeclaration operation, DeclarationConstruct declaration)
    {
        this.operation = operation;
        this.declaration = declaration;
    }

    /**
     * @return a {@link WithParameters} which allows adding {@link ParameterDeclaration}
     * to this construct
     */
    public WithParameters with()
    {
        return new WithParameters(this, getRootConstruct());
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
     * @return the root {@link DeclarationConstruct}
     */
    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }

    /**
     * Specifies the {@link OperationImplementation} for the {@link Operation}
     *
     * @param implementation a {@link OperationImplementation}
     * @return {@value this} construct
     */
    public OperationConstruct implementedIn(OperationImplementation implementation)
    {
        operation.setImplementation(implementation);
        return this;
    }

    /**
     * Adds another operation to the root {@link DeclarationConstruct}
     *
     * @param name the name of the operation
     * @return a new {@link OperationConstruct}
     */
    public OperationConstruct withOperation(String name)
    {
        return getRootConstruct().withOperation(name);
    }

    /**
     * Adds another configuration to the root {@link DeclarationConstruct}
     *
     * @param name the name of the configuration
     * @return a new {@link ConfigurationConstruct}
     */
    public ConfigurationConstruct withConfig(String name)
    {
        return getRootConstruct().withConfig(name);
    }

    /**
     * Adds the given capability to the declaring operation
     *
     * @param capability a not {@code null} capability
     * @return {@value this} construct
     */
    @Override
    public OperationConstruct withCapability(Object capability)
    {
        operation.addCapability(capability);
        return this;
    }
}
