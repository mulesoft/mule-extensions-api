/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

/**
 * A bridge which allows adding {@link ParameterDeclaration} to
 * a {@link #owner} that belongs to a {@link DeclarationConstruct}
 *
 * @since 1.0
 */
public final class WithParameters
{

    private final HasParameters owner;
    private final DeclarationConstruct declaration;

    protected WithParameters(HasParameters owner, DeclarationConstruct declaration)
    {
        this.owner = owner;
        this.declaration = declaration;
    }

    /**
     * Adds a required parameter
     *
     * @param name the name of the parameter
     * @return a new {@link ParameterConstruct}
     */
    public ParameterConstruct requiredParameter(String name)
    {
        return new ParameterConstruct(owner, newParameter(name, true), declaration);
    }

    /**
     * Adds an optional parameter
     *
     * @param name the name of the parameter
     * @return a new {@link OptionalParameterConstruct}
     */
    public OptionalParameterConstruct optionalParameter(String name)
    {
        return new OptionalParameterConstruct(owner, newParameter(name, false), declaration);
    }

    /**
     * Adds another config of the given name
     *
     * @param name the name of the config
     * @return a new {@link ConfigurationConstruct}
     */
    public ConfigurationConstruct withConfig(String name)
    {
        return declaration.withConfig(name);
    }

    /**
     * Adds another operation of the given name
     *
     * @param name the name of the config
     * @return a new {@link OperationConstruct}
     */
    public OperationConstruct withOperation(String name)
    {
        return declaration.withOperation(name);
    }

    private ParameterDeclaration newParameter(String name, boolean required)
    {
        ParameterDeclaration parameter = new ParameterDeclaration();
        parameter.setName(name);
        parameter.setRequired(required);
        owner.addParameter(parameter);

        return parameter;
    }
}
