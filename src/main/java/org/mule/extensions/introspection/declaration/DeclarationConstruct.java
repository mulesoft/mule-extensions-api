/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

public final class DeclarationConstruct implements Construct, HasCapabilities<DeclarationConstruct>
{

    private final Declaration declaration;

    public DeclarationConstruct(String name, String version)
    {
        declaration = new Declaration(name, version);
    }

    public DeclarationConstruct describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    public ConfigurationConstruct withConfig(String name)
    {
        ConfigurationDeclaration config = new ConfigurationDeclaration(name);
        declaration.addConfig(config);

        return new ConfigurationConstruct(config, this);
    }

    public OperationConstruct withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        declaration.addOperation(operation);

        return new OperationConstruct(operation, this);
    }

    @Override
    public DeclarationConstruct withCapability(Object capability)
    {
        declaration.addCapability(capability);
        return this;
    }

    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return this;
    }

    public Declaration getDeclaration()
    {
        return declaration;
    }
}
