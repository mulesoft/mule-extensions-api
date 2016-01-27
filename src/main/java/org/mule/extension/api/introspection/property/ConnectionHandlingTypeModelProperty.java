/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

import org.mule.api.connection.ConnectionProvider;
import org.mule.extension.api.introspection.PoolingSupport;

/**
 * A model property for {@link org.mule.extension.api.introspection.ConnectionProviderModel} which describes
 * the Connection Handling Type for a given {@link ConnectionProvider}.
 *
 * @since 1.0
 */
public interface ConnectionHandlingTypeModelProperty
{

    /**
     * A unique key that identifies this property type
     */
    public static final String KEY = ConnectionHandlingTypeModelProperty.class.getName();

    /**
     * To signal that the connection handling supports cache.
     */
    boolean isCached();

    /**
     * To signal that it has no connection handling strategy.
     */
    boolean isNone();

    /**
     * To signal that the connection handling supports pooling.
     */
    boolean isPooled();

    /**
     * Return the type of {@link PoolingSupport} that the given ConnectionProvider supports.
     */
    PoolingSupport getPoolingSupport();
}
