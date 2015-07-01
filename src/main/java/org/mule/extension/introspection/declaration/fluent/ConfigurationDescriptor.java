/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

import org.mule.extension.introspection.ConfigurationInstantiator;

/**
 * A {@link Descriptor} which allows configuring a {@link ConfigurationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public final class ConfigurationDescriptor extends HasParameters implements Descriptor, HasCapabilities<ConfigurationDescriptor>
{

    private final DeclarationDescriptor declaration;
    private final ConfigurationDeclaration config;

    ConfigurationDescriptor(ConfigurationDeclaration config, DeclarationDescriptor declaration)
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
        return new WithParameters(this, getRootDeclaration());
    }

    /**
     * Adds a description to the configuration
     *
     * @param description a description
     * @return this descriptor
     */
    public ConfigurationDescriptor describedAs(String description)
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
     * @return this descriptor
     */
    @Override
    public ConfigurationDescriptor withCapability(Object capability)
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
     * @return this descriptor
     */
    public ConfigurationDescriptor instantiatedWith(ConfigurationInstantiator configurationInstantiator)
    {
        config.setConfigurationInstantiator(configurationInstantiator);
        return this;
    }

    /**
     * Adds another config of the given name
     *
     * @param name the name of the config
     * @return a new {@link ConfigurationDescriptor}
     */
    public ConfigurationDescriptor withConfig(String name)
    {
        return declaration.withConfig(name);
    }

    /**
     * Returns the root {@link DeclarationDescriptor}
     *
     * @return a {@link DeclarationDescriptor}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return declaration;
    }
}
