/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.runtime.InterceptorFactory;

/**
 * A {@link Descriptor} which allows configuring a {@link ConfigurationDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class ConfigurationDescriptor extends HasParameters implements
        Descriptor, HasModelProperties<ConfigurationDescriptor>, HasInterceptors<ConfigurationDescriptor>
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
     * {@inheritDoc}
     */
    @Override
    public ConfigurationDescriptor withModelProperty(String key, Object value)
    {
        config.addModelProperty(key, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationDescriptor withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        config.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * Specifies the {@link ConfigurationFactory} to be used to
     * create instances of objects which are compliant with the declared
     * configuration
     *
     * @param configurationFactory a {@link ConfigurationFactory}
     * @return this descriptor
     */
    public ConfigurationDescriptor createdWith(ConfigurationFactory configurationFactory)
    {
        config.setConfigurationFactory(configurationFactory);
        return this;
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
