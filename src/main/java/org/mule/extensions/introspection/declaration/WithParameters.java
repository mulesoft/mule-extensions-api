/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

public final class WithParameters
{

    private final HasParameters owner;
    private final DeclarationConstruct declaration;

    protected WithParameters(HasParameters owner, DeclarationConstruct declaration)
    {
        this.owner = owner;
        this.declaration = declaration;
    }

    public ParameterConstruct requiredParameter(String name)
    {
        return new ParameterConstruct(owner, newParameter(name, true), declaration);
    }

    public OptionalParameterConstruct optionalParameter(String name)
    {
        return new OptionalParameterConstruct(owner, newParameter(name, false), declaration);
    }

    public ConfigurationConstruct withConfig(String name)
    {
        return declaration.withConfig(name);
    }

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
