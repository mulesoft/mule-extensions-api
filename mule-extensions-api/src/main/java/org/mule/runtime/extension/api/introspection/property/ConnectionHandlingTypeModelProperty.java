/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.api.connection.ConnectionHandlingStrategy;
import org.mule.runtime.api.connection.ConnectionHandlingStrategyFactory;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.PoolingListener;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.connection.PoolingSupport;

/**
 * A model property for the {@link ConnectionProviderModel} which describes
 * the Connection Handling Type for a given {@link ConnectionProvider}.
 *
 * @since 1.0
 */
public final class ConnectionHandlingTypeModelProperty implements ModelProperty
{

    private boolean cached = false;
    private PoolingSupport poolingSupport = PoolingSupport.NOT_SUPPORTED;
    private boolean none = false;

    public ConnectionHandlingTypeModelProperty(ConnectionProvider connectionProvider)
    {
        connectionProvider.getHandlingStrategy(new ConnectionHandlingStrategyFactory()
        {
            @Override
            public ConnectionHandlingStrategy supportsPooling()
            {
                poolingSupport = PoolingSupport.SUPPORTED;
                return null;
            }

            @Override
            public ConnectionHandlingStrategy supportsPooling(PoolingListener poolingListener)
            {
                return supportsPooling();
            }

            @Override
            public ConnectionHandlingStrategy requiresPooling()
            {
                poolingSupport = PoolingSupport.REQUIRED;
                return null;
            }

            @Override
            public ConnectionHandlingStrategy requiresPooling(PoolingListener poolingListener)
            {
                return requiresPooling();
            }

            @Override
            public ConnectionHandlingStrategy cached()
            {
                cached = true;
                return null;
            }

            @Override
            public ConnectionHandlingStrategy none()
            {
                none = true;
                return null;
            }
        });
    }

    /**
     * Signals that the connection handling supports cache.
     */
    public boolean isCached()
    {
        return cached;
    }

    /**
     * Signals that it has no connection handling strategy.
     */
    public boolean isNone()
    {
        return none;
    }

    /**
     * Signals that the connection handling supports pooling.
     */
    public boolean isPooled()
    {
        return !poolingSupport.equals(PoolingSupport.NOT_SUPPORTED);
    }

    /**
     * Return the type of {@link PoolingSupport} that the given ConnectionProvider supports.
     */
    public PoolingSupport getPoolingSupport()
    {
        return poolingSupport;
    }

    /**
     * {@inheritDoc}
     * @return {@code Connection Handling Type}
     */
    @Override
    public String getName()
    {
        return "Connection Handling Type";
    }

    /**
     * {@inheritDoc}
     * @return {@code true}
     */
    @Override
    public boolean isExternalizable()
    {
        return true;
    }
}
