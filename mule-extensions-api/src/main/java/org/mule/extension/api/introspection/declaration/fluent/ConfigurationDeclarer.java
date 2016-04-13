/*
/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.config.ConfigurationFactory;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

/**
 * Allows configuring a {@link ConfigurationDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class ConfigurationDeclarer extends ParameterizedDeclarer implements
        HasOperationDeclarer, HasConnectionProviderDeclarer, HasSourceDeclarer,
        HasModelProperties<ConfigurationDeclarer>, HasInterceptors<ConfigurationDeclarer>
{

    private final ConfigurationDeclaration declaration;

    /**
     * Creates a new instance
     *
     * @param declaration the declaration object to be configured
     * @param typeLoader  a {@link ClassTypeLoader} used to create the {@link MetadataType types}
     */
    ConfigurationDeclarer(ConfigurationDeclaration declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
        this.declaration = declaration;
    }

    /**
     * Adds a description to the configuration
     *
     * @param description a description
     * @return {@code this} declarer
     */
    public ConfigurationDeclarer describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        declaration.addOperation(operation);

        return new OperationDeclarer(operation, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionProviderDeclarer withConnectionProvider(String name)
    {
        ConnectionProviderDeclaration declaration = new ConnectionProviderDeclaration(name);
        this.declaration.addConnectionProvider(declaration);

        return new ConnectionProviderDeclarer(declaration, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDeclarer withMessageSource(String name)
    {
        SourceDeclaration declaration = new SourceDeclaration(name);
        this.declaration.addMessageSource(declaration);

        return new SourceDeclarer(declaration, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationDeclarer withModelProperty(ModelProperty modelProperty)
    {
        declaration.addModelProperty(modelProperty);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationDeclarer withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        declaration.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * Specifies the {@link ConfigurationFactory} to be used to
     * create instances of objects which are compliant with the declared
     * configuration
     *
     * @param configurationFactory a {@link ConfigurationFactory}
     * @return {@code this} declarer
     */
    public ConfigurationDeclarer createdWith(ConfigurationFactory configurationFactory)
    {
        declaration.setConfigurationFactory(configurationFactory);
        return this;
    }
}
