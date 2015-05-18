/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension;

import org.mule.extension.introspection.Configuration;
import org.mule.extension.introspection.Describer;
import org.mule.extension.introspection.Extension;
import org.mule.extension.introspection.ExtensionFactory;
import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.Parameter;
import org.mule.extension.runtime.ConfigurationInstanceProvider;
import org.mule.extension.runtime.OperationContext;
import org.mule.extension.runtime.OperationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Manages the {@link Extension}s available in the current context and their state.
 * Going beyond the introspection model defined by {@link Extension}, {@link Configuration}
 * and {@link Operation} class, at runtime there will be instances implementing the extension
 * components modeled by them. Those instances need to be registered with this manager in order to be used.
 * <p/>
 * The workflow for this manager would be to first discover the currently available extensions
 * through {@link #discoverExtensions(ClassLoader)}. Additionally, {@link Extension} instances
 * can also be added in runtime through {@link #registerExtension(Extension)}.
 * <p/>
 * Instances serving as realization of a {@link Configuration} model need to be registered
 * with the {@link #registerConfigurationInstanceProvider(String, ConfigurationInstanceProvider)}
 * method in orderfor this manager to provision the underlying infrastructure to host and execute such configuration.
 * Oncea configuration is registered, then operations can be executed with it by requesting a {@link OperationExecutor}
 * to the {@link #getOperationExecutor(String, OperationContext)} or {@link #getOperationExecutor(OperationContext)}
 * methods
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
     * <li>Standard Java SPI mechanism is used to discover implementations of the {@link Describer} interface</li>
     * <li>The discovered describers are fed into a {@link ExtensionFactory} and transformed in {@link Extension} instances</li>
     * <li>Those extensions are registered through the {@link #registerExtension(Extension)} method</li>
     * </ul>
     * Finally, a {@link java.util.List} will be returned with the {@link Extension}s
     * that were registered or modified. For instance, the first time this method is invoked, all the discovered extensions
     * will be returned. If it's then called again, the extensions that are already registered will not be present
     * in the newly returned list, only those that were not there the first time or that were modified. If no extensions
     * are found, or nothing has changed since the last discovery, then an empty list is returned
     *
     * @param classLoader a not {@code null} {@link java.lang.ClassLoader} in which to search for extensions
     * @return a {@link java.util.List} with the registered or updated {@link Extension}s or
     * an empty list if none is found or updated
     */
    List<Extension> discoverExtensions(ClassLoader classLoader);

    /**
     * Registers the given {@link Extension}
     * If a matching {@link Extension} is already registered,
     * then this manager will apply whatever policy finds more convenient to decide if the existing
     * registration is kept or replaced. That is up to the implementation. The only thing this contract
     * guarantees is that each extension type will be registered only once and that the discovery process
     * will not fail if many discovery runs are triggered over the same classpath.
     *
     * @param extension the {@link Extension} to be registered. Cannot be {@code null}
     * @return {@code true} if the extension was registered. {@code false} if the platform decided
     * to kept an existing declaration
     */
    boolean registerExtension(Extension extension);

    /**
     * Returns a {@link Set} listing all the available
     * {@link Extension}s.
     *
     * @return an {@link Set}. Will not be {@code null} but might be empty
     */
    Set<Extension> getExtensions();

    /**
     * Returns a {@link Set} with the {@link Extension}s
     * that have the given capability
     *
     * @param capability a {@link java.lang.Class} representing a capability
     * @return a {@link Set} with the matching {@link Extension}s. It might
     * be {@code null} but will never be empty
     */
    <C> Set<Extension> getExtensionsCapableOf(Class<C> capability);


    /**
     * Registers the {@code configurationInstanceProvider}.
     * The provider can later be referenced by the given
     * {@code providerName}
     *
     * @param providerName                  the name under which the {@code configurationInstanceProvider} will be referenced
     * @param configurationInstanceProvider a {@link ConfigurationInstanceProvider}
     * @param <C>                           the type of the configurations instances that {@code configurationInstanceProvider} provides
     */
    <C> void registerConfigurationInstanceProvider(String providerName, ConfigurationInstanceProvider<C> configurationInstanceProvider);

    /**
     * Provisions a {@link OperationExecutor} to execute the
     * {@link Operation} that is referenced by {@code operationContext}.
     * This method also requires referencing the {@link ConfigurationInstanceProvider}
     * that will be used to obtain a configuration instance. That reference
     * is done through the {@code configurationInstanceProviderName} parameter
     * which must point to a {@link ConfigurationInstanceProvider} previously
     * registered through {@link #registerConfigurationInstanceProvider(String, ConfigurationInstanceProvider)}
     *
     * @param configurationInstanceProviderName the name of the {@link ConfigurationInstanceProvider} used to obtain a configuration instance
     * @param operationContext                  a {@link OperationContext}
     * @return a functional {@link OperationExecutor}
     */
    OperationExecutor getOperationExecutor(String configurationInstanceProviderName, OperationContext operationContext);

    /**
     * Provisions a {@link OperationExecutor} to execute the {@link Operation}
     * that is referenced by {@code operationContext}.
     * <p/>
     * Because this method makes no reference to a particular
     * {@link ConfigurationInstanceProvider}, the platform will choose
     * one by following criteria:
     * <ul>
     * <li>If only one {@link ConfigurationInstanceProvider} was registered
     * through {@link #registerConfigurationInstanceProvider(String, ConfigurationInstanceProvider)}
     * for the {@link Extension} that owns the {@link Operation}, then that provider will be used</li>
     * <li>If more than one were registered, then an {@link IllegalStateException} will be thrown</li>
     * <li>If no {@link ConfigurationInstanceProvider} was registered, then the platform
     * will evaluate all the {@link Configuration}s on the {@link Extension} looking for those
     * which can be created implicitly. If any are found, then the first one is selected and a
     * {@link ConfigurationInstanceProvider} will be created and registered for it.
     * </ul>
     * <p/>
     * It is considered that a {@link Configuration} can be created implicitly
     * if for all its {@link Parameter}s it can be said that they're optional
     * or/and have a default value. In such case, the platform will create an
     * instance which respects those defaults and has {@code null} values for
     * the rest of the parameters.
     *
     * @param operationContext a {@link OperationContext}
     * @return a functional {@link OperationExecutor}
     */
    OperationExecutor getOperationExecutor(OperationContext operationContext);

}
