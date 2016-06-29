/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.connection;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.beans.Transient;
import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link ConnectionProviderModel}
 *
 * @param <Connection> the generic type for the connections that the returned  {@link ConnectionProvider providers} produce
 * @since 1.0
 */
public final class ImmutableRuntimeConnectionProviderModel<Connection> extends ImmutableConnectionProviderModel<Connection> implements RuntimeConnectionProviderModel<Connection>
{

    private transient Class<Connection> connectionType;
    private transient final ConnectionProviderFactory connectionProviderFactory;

    /**
     * Creates a new instance with the given state
     *
     * @param name                      the provider's name
     * @param description               the provider's description
     * @param connectionType            the {@link Class} of the provided connections
     * @param connectionProviderFactory the {@link ConnectionProviderFactory} used to create realizations of {@code this} model
     * @param parameterModels           a {@link List} with the provider's {@link ParameterModel parameterModels}
     * @param modelProperties           A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType} are {@code null}
     */
    public ImmutableRuntimeConnectionProviderModel(String name,
                                                   String description,
                                                   Class<Connection> connectionType,
                                                   ConnectionProviderFactory connectionProviderFactory,
                                                   List<ParameterModel> parameterModels,
                                                   Set<ModelProperty> modelProperties)
    {
        super(name, description, parameterModels, modelProperties);

        checkArgument(connectionType != null, "connectionType cannot be null");
        checkArgument(connectionProviderFactory != null, "connectionProviderFactory cannot be null");

        this.connectionType = connectionType;
        this.connectionProviderFactory = connectionProviderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public ConnectionProviderFactory getConnectionProviderFactory()
    {
        return connectionProviderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public Class<Connection> getConnectionType()
    {
        return connectionType;
    }
}
