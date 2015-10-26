/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

/**
 * A bridge which allows adding {@link ParameterDeclaration} to
 * a {@link #owner} that belongs to a {@link DeclarationDescriptor}
 *
 * @since 1.0
 */
public final class WithParameters
{

    private final HasParameters owner;
    private final DeclarationDescriptor declaration;

    protected WithParameters(HasParameters owner, DeclarationDescriptor declaration)
    {
        this.owner = owner;
        this.declaration = declaration;
    }

    /**
     * Adds a required parameter
     *
     * @param name the name of the parameter
     * @return a new {@link ParameterDescriptor}
     */
    public ParameterDescriptor requiredParameter(String name)
    {
        return new ParameterDescriptor(owner, newParameter(name, true), declaration);
    }

    /**
     * Adds an optional parameter
     *
     * @param name the name of the parameter
     * @return a new {@link OptionalParameterDescriptor}
     */
    public OptionalParameterDescriptor optionalParameter(String name)
    {
        return new OptionalParameterDescriptor(owner, newParameter(name, false), declaration);
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
