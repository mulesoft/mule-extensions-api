/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import org.mule.extensions.introspection.ConfigurationInstantiator;

/**
 * A {@link Construct} which allows configuring a {@link ConfigurationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public final class ConfigurationConstruct extends HasParameters implements Construct, HasCapabilities<ConfigurationConstruct>
{

    private final DeclarationConstruct declaration;
    private final ConfigurationDeclaration config;

    ConfigurationConstruct(ConfigurationDeclaration config, DeclarationConstruct declaration)
    {
        this.config = config;
        this.declaration = declaration;
    }

    /**
     * @return a {@link WithParameters} which allows adding parameters
     * to the declaring configuration
     */
    public WithParameters with()
    {
        return new WithParameters(this, getRootConstruct());
    }

    /**
     * Adds a description to the configuration
     *
     * @param description a description
     * @return this construct
     */
    public ConfigurationConstruct describedAs(String description)
    {
        config.setDescription(description);
        return this;
    }

    /**
     * Adds the given {@code parameter} to the declaring configuration
     *
     * @param parameter a {@link ParameterDeclaration}
     */
    @Override
    protected void addParameter(ParameterDeclaration parameter)
    {
        config.getParameters().add(parameter);
    }

    /**
     * Adds the given capability to the declaring configuration
     *
     * @param capability a not {@code null} capability
     * @return this construct
     */
    @Override
    public ConfigurationConstruct withCapability(Object capability)
    {
        config.addCapability(capability);
        return this;
    }

    /**
     * Specifies the {@link ConfigurationInstantiator} to be used to
     * create instances of objects which are compliant with the declared
     * configuration
     *
     * @param configurationInstantiator a {@link ConfigurationInstantiator}
     * @return this construct
     */
    public ConfigurationConstruct instantiatedWith(ConfigurationInstantiator configurationInstantiator)
    {
        config.setConfigurationInstantiator(configurationInstantiator);
        return this;
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
     * Returns the owning {@link DeclarationConstruct}
     *
     * @return a {@link DeclarationConstruct}
     */
    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }
}
