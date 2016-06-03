/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderFactory;

/**
 * Allows configuring a {@link ConnectionProviderDeclaration} through a fluent API
 *
 * @since 1.0
 */
public final class ConnectionProviderDeclarer extends ParameterizedDeclarer<ConnectionProviderDeclaration> implements HasModelProperties<ConnectionProviderDeclarer>
{

    /**
     * Creates a new instance
     *
     * @param declaration the declaration object to be configured
     * @param typeLoader  a {@link ClassTypeLoader} used to create the {@link MetadataType types}
     */
    public ConnectionProviderDeclarer(ConnectionProviderDeclaration declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
    }

    /**
     * Adds a description to the provider
     *
     * @param description a description
     * @return {@code this} declarer
     */
    public ConnectionProviderDeclarer describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * Specifies the {@link ConnectionProviderFactory} to be used to
     * create instances of the {@link ConnectionProvider}s which are compliant
     * with the {@link #declaration}
     *
     * @param factory a {@link ConnectionProviderFactory}
     * @return {@code this} declarer
     */
    public ConnectionProviderDeclarer createdWith(ConnectionProviderFactory factory)
    {
        declaration.setFactory(factory);
        return this;
    }

    /**
     * The {@link Class} to which the first generic in the {@link ConnectionProvider}
     * interface is resolved to on the instances that the {@link #declaration} describes
     *
     * @param configType a {@link Class}
     * @return {@code this} declarer
     */
    public ConnectionProviderDeclarer forConfigsOfType(Class<?> configType)
    {
        declaration.setConfigurationType(configType);
        return this;
    }

    /**
     * The {@link Class} to which the second generic in the {@link ConnectionProvider}
     * interface is resolved to on the instances that the {@link #declaration} describes
     *
     * @param connectionType a {@link Class}
     * @return {@code this} declarer
     */
    public ConnectionProviderDeclarer whichGivesConnectionsOfType(Class<?> connectionType)
    {
        declaration.setConnectionType(connectionType);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionProviderDeclarer withModelProperty(ModelProperty modelProperty)
    {
        declaration.addModelProperty(modelProperty);
        return this;
    }
}
