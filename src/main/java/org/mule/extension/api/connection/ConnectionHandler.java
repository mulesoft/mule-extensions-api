/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.connection;

/**
 * Handles connections that are created from a config of the generic type
 * {@code Config}.
 * <p/>
 * Implementations are expected to be reusable and thread-safe
 *
 * @param <Config>     the generic type of the objects to be used as configs
 * @param <Connection> the generic type of the connections to be handled
 * @since 1.0
 */
//TODO: Consider moving this to the mule-api when we actually create that artifact
public interface ConnectionHandler<Config, Connection>
{

    /**
     * Creates a new connection from the given {@code config}.
     * <p/>
     * The returned connection is expected to be ready to use
     *
     * @param config a configuration which parametrizes the connection's creation
     * @return a ready to use {@code Connection}
     */
    Connection connect(Config config);

    /**
     * Disposes the given {@code connection}, freeing all its allocated resources
     *
     * @param connection a non {@code null} {@code Connection}.
     */
    void disconnect(Connection connection);
}
