/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.metadata.api.ClassTypeLoader;

import java.util.Optional;

/**
 * A builder object which allows creating an {@link ExtensionDeclaration}
 * through a fluent API.
 *
 * @since 1.0
 */
public class ExtensionDeclarer implements HasModelProperties<ExtensionDeclarer>, HasExceptionEnricher<ExtensionDeclarer>,
        HasOperationDeclarer, HasConnectionProviderDeclarer, HasSourceDeclarer
{

    private final ExtensionDeclaration extensionDeclaration;
    private final ClassTypeLoader typeLoader;

    /**
     * Constructor for this descriptor
     */
    public ExtensionDeclarer()
    {
        extensionDeclaration = new ExtensionDeclaration();
        typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    }

    /**
     * Provides the extension's name
     *
     * @param name the extension's name
     * @return {@code this} descriptor
     */
    public ExtensionDeclarer named(String name)
    {
        extensionDeclaration.setName(name);
        return this;
    }

    /**
     * Provides the extension's version
     *
     * @param version the extensions version
     * @return {@code this} descriptor
     */
    public ExtensionDeclarer onVersion(String version)
    {
        extensionDeclaration.setVersion(version);
        return this;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} descriptor
     */
    public ExtensionDeclarer describedAs(String description)
    {
        extensionDeclaration.setDescription(description);
        return this;
    }

    /**
     * Adds a config of the given name
     *
     * @param name a non blank name
     * @return a {@link ConfigurationDeclarer} which allows describing the created configuration
     */
    public ConfigurationDeclarer withConfig(String name)
    {
        ConfigurationDeclaration config = new ConfigurationDeclaration(name);
        extensionDeclaration.addConfig(config);

        return new ConfigurationDeclarer(config, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionProviderDeclarer withConnectionProvider(String name)
    {
        ConnectionProviderDeclaration declaration = new ConnectionProviderDeclaration(name);
        this.extensionDeclaration.addConnectionProvider(declaration);

        return new ConnectionProviderDeclarer(declaration, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        extensionDeclaration.addOperation(operation);

        return new OperationDeclarer(operation, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDeclarer withMessageSource(String name)
    {
        SourceDeclaration declaration = new SourceDeclaration(name);
        this.extensionDeclaration.addMessageSource(declaration);

        return new SourceDeclarer(declaration, typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtensionDeclarer withModelProperty(ModelProperty value)
    {
        extensionDeclaration.addModelProperty(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtensionDeclarer withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory)
    {
        extensionDeclaration.setExceptionEnricherFactory(enricherFactory);
        return this;
    }

    public ExtensionDeclaration getDeclaration()
    {
        return extensionDeclaration;
    }

    /**
     * @return the configured {@link ExtensionDeclaration}
     */
    public ExtensionDeclaration getExtensionDeclaration()
    {
        return extensionDeclaration;
    }

    public ExtensionDeclarer fromVendor(String vendor)
    {
        extensionDeclaration.setVendor(vendor);
        return this;
    }
}
