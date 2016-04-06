/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;


import org.mule.api.connection.ConnectionProvider;

/**
 * Creates instances of {@link ConnectionProvider} for the generic
 * {@code Config} and {@code Connection} types.
 * <p>
 * Instances are thread-safe and reusable.
 *
 * @param <Config>     the generic type for the configuration objects that the returned {@link ConnectionProvider providers} accept
 * @param <Connection> the generic type for the connections that the returned  {@link ConnectionProvider providers} produce
 * @since 1.0
 */
public interface ConnectionProviderFactory<Config, Connection>
{

    /**
     * @return a new {@link ConnectionProvider}
     */
    ConnectionProvider<Config, Connection> newInstance();

    /**
     * Returns the concrete type of the object to be returned by this instance
     *
     * @return a {@link Class}
     */
    Class<? extends ConnectionProvider> getObjectType();
}
