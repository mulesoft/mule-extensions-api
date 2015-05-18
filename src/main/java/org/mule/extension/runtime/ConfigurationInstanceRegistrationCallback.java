/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.Configuration;

/**
 * A callback to be used by components which creates
 * new instances that are the realization of a
 * {@link Configuration} model. Every component which
 * creates such instances must notify this callback
 * in order for the platform to start managing such instance
 *
 * @since 1.0
 */
public interface ConfigurationInstanceRegistrationCallback
{

    /**
     * Notifies the creations of {@code configurationInstance}
     * by the given {@code configurationInstanceProvider}
     *
     * @param configurationInstanceProvider the {@link ConfigurationInstanceProvider} which created the instance
     * @param configurationInstance         the created instance
     * @param <C>                           the type of the created instance
     */
    <C> void registerNewConfigurationInstance(ConfigurationInstanceProvider<C> configurationInstanceProvider,
                                              C configurationInstance);
}
