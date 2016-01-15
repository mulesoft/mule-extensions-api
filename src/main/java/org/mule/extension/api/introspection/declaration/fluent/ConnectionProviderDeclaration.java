/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConfigurationModel;
import org.mule.extension.api.introspection.ConnectionProviderFactory;
import org.mule.extension.api.introspection.ConnectionProviderModel;

import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link ConnectionProviderModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public final class ConnectionProviderDeclaration extends BaseDeclaration<ConnectionProviderDeclaration> implements ParameterizedDeclaration
{
    private final List<ParameterDeclaration> parameters = new LinkedList<>();
    private ConnectionProviderFactory factory;
    private Class<?> configurationType;
    private Class<?> connectionType;

    /**
     * {@inheritDoc}
     */
    ConnectionProviderDeclaration(String name)
    {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParameterDeclaration> getParameters()
    {
        return parameters;
    }

    public ConnectionProviderFactory getFactory()
    {
        return factory;
    }

    void setFactory(ConnectionProviderFactory factory)
    {
        this.factory = factory;
    }

    public Class<?> getConfigurationType()
    {
        return configurationType;
    }

    void setConfigurationType(Class<?> configurationType)
    {
        this.configurationType = configurationType;
    }

    public Class<?> getConnectionType()
    {
        return connectionType;
    }

    void setConnectionType(Class<?> connectionType)
    {
        this.connectionType = connectionType;
    }
}
