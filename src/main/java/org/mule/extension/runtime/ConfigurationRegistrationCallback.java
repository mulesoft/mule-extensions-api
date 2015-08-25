/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.ConfigurationModel;
import org.mule.extension.introspection.ExtensionModel;

/**
 * A callback to be used by components which creates new instances that are the realization of a
 * {@link ConfigurationModel}. Every component which creates such instances must notify this callback
 * in order for the platform to start managing such instance.
 *
 * @since 1.0
 */
public interface ConfigurationRegistrationCallback
{

    /**
     * Registers the given {@code configuration}
     *
     * @param extensionModel            The {@link ExtensionModel} model for the {@code configuration}
     * @param configurationProviderName the name of a registered {@link ConfigurationProvider} which owns the instance
     * @param configuration             an instance that is a realisation of the {@link ConfigurationModel}
     * @param <C>                       the generic type of the {@code configuration}
     * @return the name under which the {@code configuration} was registered on the mule registry
     */
    <C> String registerConfiguration(ExtensionModel extensionModel, String configurationProviderName, C configuration);
}
