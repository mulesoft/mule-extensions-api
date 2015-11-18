/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;


import org.mule.api.connection.ConnectionProvider;

/**
 * Introspection model for {@link ConnectionProvider} types.
 *
 * @param <Config>     the generic type for the configuration objects that the returned {@link ConnectionProvider providers} accept
 * @param <Connection> the generic type for the connections that the returned  {@link ConnectionProvider providers} produce
 * @since 1.0
 */
public interface ConnectionProviderModel<Config, Connection> extends Described, EnrichableModel, ParametrizedModel
{

    /**
     * @return the {@link ConnectionProviderFactory} to be used to create instances
     * consistent with this model
     */
    ConnectionProviderFactory getConnectionProviderFactory();

    /**
     * @return The {@link Class} object for the {@code Config} type
     */
    Class<Config> getConfigurationType();

    /**
     * @return The {@link Class} object for the {@code Connection} type
     */
    Class<Connection> getConnectionType();
}
