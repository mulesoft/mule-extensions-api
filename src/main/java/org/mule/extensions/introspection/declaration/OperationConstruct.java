/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

public final class OperationConstruct extends HasParameters implements Construct
{

    private final OperationDeclaration operation;
    private final DeclarationConstruct declaration;

    OperationConstruct(OperationDeclaration operation, DeclarationConstruct declaration)
    {
        this.operation = operation;
        this.declaration = declaration;
    }

    public OperationConstruct describedAs(String description)
    {
        operation.setDescription(description);
        return this;
    }

    public WithParameters with()
    {
        return new WithParameters(this, getRootConstruct());
    }

    @Override
    protected void addParameter(ParameterDeclaration parameter)
    {
        operation.addParameter(parameter);
    }

    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }

    public OperationConstruct withOperation(String name)
    {
        return getRootConstruct().withOperation(name);
    }

    public ConfigurationConstruct withConfig(String name)
    {
        return getRootConstruct().withConfig(name);
    }
}
