/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.metadata.api.ClassTypeLoader;

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
    private final ClassTypeLoader typeLoader;

    protected WithParameters(HasParameters owner, DeclarationDescriptor declaration, ClassTypeLoader typeLoader)
    {
        this.owner = owner;
        this.declaration = declaration;
        this.typeLoader = typeLoader;
    }

    /**
     * Adds a required parameter
     *
     * @param name the name of the parameter
     * @return a new {@link ParameterDescriptor}
     */
    public ParameterDescriptor requiredParameter(String name)
    {
        return new ParameterDescriptor(owner, newParameter(name, true), declaration, typeLoader);
    }

    /**
     * Adds an optional parameter
     *
     * @param name the name of the parameter
     * @return a new {@link OptionalParameterDescriptor}
     */
    public OptionalParameterDescriptor optionalParameter(String name)
    {
        return new OptionalParameterDescriptor(owner, newParameter(name, false), declaration, typeLoader);
    }

    private ParameterDeclaration newParameter(String name, boolean required)
    {
        ParameterDeclaration parameter = new ParameterDeclaration(name);
        parameter.setRequired(required);
        owner.addParameter(parameter);

        return parameter;
    }
}
