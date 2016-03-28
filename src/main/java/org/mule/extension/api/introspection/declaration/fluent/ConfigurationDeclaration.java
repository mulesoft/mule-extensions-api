/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.introspection.ConfigurationModel;

import java.util.List;

/**
 * A declaration object for a {@link ConfigurationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ConfigurationDeclaration extends ParameterizedInterceptableDeclaration<ConfigurationDeclaration>
{

    private final SubDeclarationsContainer subDeclarations = new SubDeclarationsContainer();
    private ConfigurationFactory configurationFactory;

    /**
     * {@inheritDoc}
     */
    ConfigurationDeclaration(String name)
    {
        super(name);
    }

    public ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    public void setConfigurationFactory(ConfigurationFactory configurationFactory)
    {
        this.configurationFactory = configurationFactory;
    }

    /**
     * @return an unmodifiable {@link List} with
     * the available {@link OperationDeclaration}s
     */
    public List<OperationDeclaration> getOperations()
    {
        return subDeclarations.getOperations();
    }

    /**
     * @return an unmodifiable {@link List} with the available {@link ConnectionProviderDeclaration}s
     */
    public List<ConnectionProviderDeclaration> getConnectionProviders()
    {
        return subDeclarations.getConnectionProviders();
    }

    /**
     * @return an unmodifiable {@link List} with the available {@link SourceDeclaration}s
     */
    public List<SourceDeclaration> getMessageSources()
    {
        return subDeclarations.getMessageSources();
    }

    /**
     * Adds a {@link ConnectionProviderDeclaration}
     *
     * @param connectionProvider a not {@code null} {@link ConnectionProviderDeclaration}
     * @return {@code this} declaration
     * @throws IllegalArgumentException if {@code connectionProvider} is {@code null}
     */
    public ConfigurationDeclaration addConnectionProvider(ConnectionProviderDeclaration connectionProvider)
    {
        subDeclarations.addConnectionProvider(connectionProvider);
        return this;
    }

    /**
     * Adds a {@link OperationDeclaration}
     *
     * @param operation a not {@code null} {@link OperationDeclaration}
     * @return {@code this} declaration
     * @throws {@link IllegalArgumentException} if {@code operation} is {@code null}
     */
    public ConfigurationDeclaration addOperation(OperationDeclaration operation)
    {
        subDeclarations.addOperation(operation);
        return this;
    }

    /**
     * Adds a {@link SourceDeclaration}
     *
     * @param sourceDeclaration a not {@code null} {@link SourceDeclaration}
     * @return {@code this} declaration
     * @throws {@link IllegalArgumentException} if {@code sourceDeclaration} is {@code null}
     */
    public ConfigurationDeclaration addMessageSource(SourceDeclaration sourceDeclaration)
    {
        subDeclarations.addMessageSource(sourceDeclaration);
        return this;
    }
}
