/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension;

import org.mule.api.extension.introspection.ConfigurationModel;
import org.mule.api.extension.introspection.OperationModel;
import org.mule.api.extension.introspection.ExtensionFactory;
import org.mule.api.extension.introspection.ExtensionModel;
import org.mule.api.extension.introspection.declaration.spi.Describer;
import org.mule.api.extension.runtime.ConfigurationProvider;
import org.mule.api.extension.runtime.ConfigurationStats;
import org.mule.api.extension.runtime.ConfigurationInstance;

import java.util.List;
import java.util.Set;

/**
 * Manages the {@link ExtensionModel extension models} available in the current context and their state.
 * <p/>
 * Going beyond the introspection model defined by {@link ExtensionModel}, {@link ConfigurationModel}
 * and {@link OperationModel} classes, at runtime there will be instances implementing the extension
 * components modeled by them. Those instances need to be registered with this manager in order to be used.
 * <p/>
 * The workflow for this manager would be to first discover the currently available extensions
 * through {@link #discoverExtensions(ClassLoader)}. Additionally, {@link ExtensionModel} instances
 * can also be added in runtime through {@link #registerExtension(ExtensionModel)}.
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
     * Registers the {@code configurationProvider}.
     *
     * @param configurationProvider a {@link ConfigurationProvider}
     * @param <C>                   the type of the configurations instances that {@code configurationProvider} provides
     */
    <C> void registerConfigurationProvider(ConfigurationProvider<C> configurationProvider);

    /**
     * Returns a {@link ConfigurationInstance} obtained through a previously registered
     * {@link ConfigurationProvider} named as {@code configurationProvider}
     * <p/>
     * After the {@link ConfigurationProvider} has been located, an instance is returned by
     * invoking its {@link ConfigurationProvider#get(Object)} with the {@code muleEvent}
     * as the argument.
     * <p/>
     * By the mere fact of this configuration being returned, the value of
     * {@link ConfigurationStats#getLastUsedMillis()} will be updated for the returned
     * {@link ConfigurationInstance}
     *
     * @param configurationProviderName the name of a previously registered {@link ConfigurationProvider}
     * @param muleEvent                 the current MuleEvent
     * @param <C>                       the generic type of the configuration instance to be returned
     * @return a {@link ConfigurationInstance}
     */
    //TODO: MULE-8946
    <C> ConfigurationInstance<C> getConfiguration(String configurationProviderName, Object muleEvent);


    /**
     * Returns a {@link ConfigurationInstance} for the given {@code extensionModel}.
     * <p/>
     * Because no {@link ConfigurationProvider} is specified, the following algorithm will
     * be applied to try and determine the instance to be returned:
     * <ul>
     * <li>If <b>one</b> {@link ConfigurationProvider} is registered, capable of handing configurations
     * of the given {@code extensionModel}, then that provider is used</li>
     * <li>If more than one {@link ConfigurationProvider} meeting the criteria above is found, then
     * a {@link IllegalStateException} is thrown</li>
     * <li>If no such {@link ConfigurationProvider} is registered, then an attempt will be made
     * to locate a {@link ConfigurationModel} for which an implicit configuration can be inferred. A model
     * can be considered implicit if all its parameters are either optional or provide a default value. If such
     * a model is found, then a {@link ConfigurationProvider} is created and registered for that model.
     * </li>
     * <li>If none of the above conditions is met, then an {@link IllegalStateException} is thrown</li>
     * </ul>
     * <p/>
     * By the mere fact of this configuration being returned, the value of
     * {@link ConfigurationStats#getLastUsedMillis()} will be updated for the returned
     * {@link ConfigurationInstance}
     *
     * @param extensionModel the {@link ExtensionModel} for which a configuration is wanted
     * @param muleEvent      the current MuleEvent
     * @param <C>            the generic type of the configuration instance to be returned
     * @return a {@link ConfigurationInstance}
     * @throws IllegalStateException if none or too many {@link ConfigurationProvider} are found to be suitable
     */
    //TODO: MULE-8946
    <C> ConfigurationInstance<C> getConfiguration(ExtensionModel extensionModel, Object muleEvent);
}
