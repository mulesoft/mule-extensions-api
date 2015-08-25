/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.Configuration;
import org.mule.extension.introspection.Extension;

/**
 * A callback to be used by components which creates new instances that are the realization of a
 * {@link Configuration} model. Every component which creates such instances must notify this callback
 * in order for the platform to start managing such instance.
 *
 * @since 1.0
 */
public interface ConfigurationInstanceRegistrationCallback
{

    /**
     * Registers the given {@code configurationInstance}
     *
     * @param extension                         The {@link Extension} model for the {@code configurationInstance}
     * @param configurationInstanceProviderName the name of a registered {@link ConfigurationInstanceProvider} which owns the instance
     * @param configurationInstance             an instance that is a realisation of the {@link Configuration} model
     * @param <C>                               the generic type of the {@code configurationInstance}
     * @return the name under which the {@code configurationInstance} was registered on the mule registry
     */
    <C> String registerConfigurationInstance(Extension extension, String configurationInstanceProviderName, C configurationInstance);
}
