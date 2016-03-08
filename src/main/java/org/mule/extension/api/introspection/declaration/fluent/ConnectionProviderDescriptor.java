/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConnectionProviderFactory;
import org.mule.metadata.api.ClassTypeLoader;

/**
 * A {@link Descriptor} which allows configuring a {@link ConnectionProviderDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public final class ConnectionProviderDescriptor extends HasParameters implements HasModelProperties<ConnectionProviderDescriptor>
{

    private final ConnectionProviderDeclaration connectionProvider;

    public ConnectionProviderDescriptor(DeclarationDescriptor declaration, ConnectionProviderDeclaration connectionProvider, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
        this.connectionProvider = connectionProvider;
    }

    public ConnectionProviderDescriptor describedAs(String description)
    {
        connectionProvider.setDescription(description);
        return this;
    }

    public ConnectionProviderDescriptor createdWith(ConnectionProviderFactory factory)
    {
        connectionProvider.setFactory(factory);
        return this;
    }

    public ConnectionProviderDescriptor forConfigsOfType(Class<?> configType)
    {
        connectionProvider.setConfigurationType(configType);
        return this;
    }

    public ConnectionProviderDescriptor whichGivesConnectionsOfType(Class<?> connectionType)
    {
        connectionProvider.setConnectionType(connectionType);
        return this;
    }

    /**
     * @return a {@link WithParameters} which allows adding {@link ParameterDeclaration}
     * to this descriptor
     */
    public WithParameters with()
    {
        return new WithParameters(this, getRootDeclaration(), typeLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return declaration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addParameter(ParameterDeclaration parameter)
    {
        connectionProvider.getParameters().add(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionProviderDescriptor withModelProperty(String key, Object value)
    {
        connectionProvider.addModelProperty(key, value);
        return this;
    }
}
