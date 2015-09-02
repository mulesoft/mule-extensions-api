/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension;

import org.mule.extension.introspection.ConfigurationModel;
import org.mule.extension.introspection.ExtensionFactory;
import org.mule.extension.introspection.ExtensionModel;
import org.mule.extension.introspection.OperationModel;
import org.mule.extension.introspection.declaration.spi.Describer;
import org.mule.extension.runtime.ConfigurationProvider;

import java.util.List;
import java.util.Set;

/**
 * Manages the {@link ExtensionModel ExtensionModels} available in the current context and their state.
 * Going beyond the introspection model defined by {@link ExtensionModel}, {@link ConfigurationModel}
 * and {@link OperationModel} class, at runtime there will be instances implementing the extension
 * components modeled by them. Those instances need to be registered with this manager in order to be used.
 * <p/>
 * The workflow for this manager would be to first discover the currently available extensions
 * through {@link #discoverExtensions(ClassLoader)}. Additionally, {@link ExtensionModel} instances
 * can also be added in runtime through {@link #registerExtension(ExtensionModel)}.
 * <p/>
 * Instances serving as realization of a {@link ConfigurationModel} need to be registered
 * with the {@link #registerConfigurationProvider(ExtensionModel, String, ConfigurationProvider)}
 * method in order for this manager to provision the underlying infrastructure to host and execute such configuration.
 *
 * @since 1.0
 */
public interface ExtensionManager
{

    /**
     * Scans the classpath visible to the given
     * {@link java.lang.ClassLoader} and registers them.
     * <p/>
     * The discovery process works as follows:
     * <ul>
     * <li>Some discovery mechanism (which one is up to the implementation) is used to discover implementations of the {@link Describer} interface</li>
     * <li>The discovered describers are fed into a {@link ExtensionFactory} and transformed in {@link ExtensionModel} instances</li>
     * <li>Those extensions are registered through the {@link #registerExtension(ExtensionModel)} method</li>
     * </ul>
     * Finally, a {@link List} is returned with all the {@link ExtensionModel extensions} available after the discovery process finishes.
     *
     * @param classLoader a not {@code null} {@link java.lang.ClassLoader} in which to search for extensions
     * @return a {@link List} with all the available {@link ExtensionModel extensions}
     */
    List<ExtensionModel> discoverExtensions(ClassLoader classLoader);

    /**
     * Registers the given {@link ExtensionModel}.
     *
     * @param extensionModel the {@link ExtensionModel} to be registered. Cannot be {@code null}
     * @throws IllegalArgumentException if an extension of the same name is already registered
     */
    void registerExtension(ExtensionModel extensionModel);

    /**
     * Returns a {@link Set} listing all the discovered
     * {@link ExtensionModel extensionModels}.
     *
     * @return an {@link Set}. Will not be {@code null} but might be empty
     */
    Set<ExtensionModel> getExtensions();

    /**
     * Returns a {@link Set} with the {@link ExtensionModel extensionModels}.
     * that have the given capability
     *
     * @param capability a {@link java.lang.Class} representing a capability
     * @return a {@link Set} with the matching {@link ExtensionModel extensionModels}.. It might
     * be {@code null} but will never be empty
     */
    <C> Set<ExtensionModel> getExtensionsCapableOf(Class<C> capability);


    /**
     * Registers the {@code configurationProvider}.
     *
     * @param configurationProvider a {@link ConfigurationProvider}
     * @param <C>                   the type of the configurations instances that {@code configurationProvider} provides
     */
    <C> void registerConfigurationProvider(ExtensionModel extensionModel, ConfigurationProvider<C> configurationProvider);
}
