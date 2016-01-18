/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExceptionEnricherFactory;

import java.util.Optional;

/**
 * A {@link Descriptor} which allows configuring a {@link Declaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class DeclarationDescriptor implements Descriptor, HasModelProperties<DeclarationDescriptor>, HasExceptionEnricher<DeclarationDescriptor>
{

    private final Declaration declaration;

    /**
     * Constructor for this descriptor
     */
    public DeclarationDescriptor()
    {
        declaration = new Declaration();
    }

    /**
     * Provides the extension's name
     *
     * @param name the extension's name
     * @return {@code this} descriptor
     */
    public DeclarationDescriptor named(String name)
    {
        declaration.setName(name);
        return this;
    }

    /**
     * Provides the extension's version
     *
     * @param version the extensions version
     * @return {@code this} descriptor
     */
    public DeclarationDescriptor onVersion(String version)
    {
        declaration.setVersion(version);
        return this;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} descriptor
     */
    public DeclarationDescriptor describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationDescriptor withConfig(String name)
    {
        ConfigurationDeclaration config = new ConfigurationDeclaration(name);
        declaration.addConfig(config);

        return new ConfigurationDescriptor(config, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDescriptor withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        declaration.addOperation(operation);

        return new OperationDescriptor(operation, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionProviderDescriptor withConnectionProvider(String name)
    {
        ConnectionProviderDeclaration declaration = new ConnectionProviderDeclaration(name);
        this.declaration.addConnectionProvider(declaration);

        return new ConnectionProviderDescriptor(this, declaration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDescriptor withMessageSource(String name)
    {
        SourceDeclaration declaration = new SourceDeclaration(name);
        this.declaration.addMessageSource(declaration);

        return new SourceDescriptor(declaration, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeclarationDescriptor withModelProperty(String key, Object value)
    {
        declaration.addModelProperty(key, value);
        return this;
    }

    /**
     * @return {@code this}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeclarationDescriptor withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory)
    {
        declaration.setExceptionEnricherFactory(enricherFactory);
        return this;
    }

    /**
     * @return the configured {@link Declaration}
     */
    public Declaration getDeclaration()
    {
        return declaration;
    }

    public DeclarationDescriptor fromVendor(String vendor)
    {
        declaration.setVendor(vendor);
        return this;
    }
}
