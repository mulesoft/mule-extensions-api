/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import org.mule.extension.api.introspection.config.ConfigurationModel;
import org.mule.extension.api.introspection.config.RuntimeConfigurationModel;

/**
 * A component responsible for providing instances which are realizations of a given {@link ConfigurationModel}.
 * <p/>
 * Instances are provided through the {@link #get(Object)} method.
 * When that method is invoked, it's up to each implementation to return a brand
 * new instance or one which has already been returned before.
 *
 * @param <T> the type of configuration instances returned
 * @since 1.0
 */
public interface ConfigurationProvider<T>
{

    /**
     * Returns a {@link ConfigurationInstance}
     * <p/>
     * This method may return an instance already returned in the past or a brand new one.
     *
     * @param muleEvent a {@link OperationContext}
     * @return a {@link ConfigurationInstance}
     */
    //TODO: MULE-8946
    ConfigurationInstance<T> get(Object muleEvent);

    /**
     * Returns the {@link RuntimeConfigurationModel} for the instances
     * returned by {@link #get(Object)}
     *
     * @return a {@link RuntimeConfigurationModel}
     */
    RuntimeConfigurationModel getModel();

    /**
     * The name under which this provider has been registered
     *
     * @return this provider's name
     */
    String getName();
}
