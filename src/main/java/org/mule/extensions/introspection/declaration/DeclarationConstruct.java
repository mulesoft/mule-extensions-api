/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

/**
 * A {@link Construct} which allows configuring a {@link Declaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class DeclarationConstruct implements Construct, HasCapabilities<DeclarationConstruct>
{

    private final Declaration declaration;

    /**
     * Constructor for this construct
     *
     * @param name    a non blank name
     * @param version a non blank version
     */
    public DeclarationConstruct(String name, String version)
    {
        declaration = new Declaration(name, version);
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return this construct
     */
    public DeclarationConstruct describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * Adds a config of the given name
     *
     * @param name a non blank name
     * @return a {@link ConfigurationConstruct} which allows describing the created configuration
     */
    public ConfigurationConstruct withConfig(String name)
    {
        ConfigurationDeclaration config = new ConfigurationDeclaration(name);
        declaration.addConfig(config);

        return new ConfigurationConstruct(config, this);
    }

    /**
     * Adds an operation of the given name
     *
     * @param name a non blank name
     * @return a {@link OperationConstruct} which allows describing the created operation
     */
    public OperationConstruct withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        declaration.addOperation(operation);

        return new OperationConstruct(operation, this);
    }

    /**
     * Adds the given capability to this declaration
     *
     * @param capability a not {@code null} capability
     * @return this construct
     */
    @Override
    public DeclarationConstruct withCapability(Object capability)
    {
        declaration.addCapability(capability);
        return this;
    }

    /**
     * @return {@value this}
     */
    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return this;
    }

    /**
     * @return the configured {@link Declaration}
     */
    public Declaration getDeclaration()
    {
        return declaration;
    }
}
