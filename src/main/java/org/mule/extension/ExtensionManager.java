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
 * with the {@link #registerConfigurationInstance(Configuration, String, Object)} method in order
 * for this manager to provision the underlying infrastructure to host and execute such configuration. Once
 * a configuration is registered, then operations can be executed with it by requesting a {@link OperationExecutor}
 * to the {@link #getOperationExecutor(Operation, Object)} method
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
     * Registers a {@code configurationInstance} which is an instance of an object which is compliant
     * with the {@link Configuration} modeled by {@code configuration}. It is mandatory for configuration
     * instances to be registered through this method before they can be used to execute operations.
     * Implementations of this method are to be considered thread-safe.
     *
     * @param configuration             a {@link Configuration} model
     * @param configurationInstanceName the name of the instance to be registered
     * @param configurationInstance     an object which is compliant with the {@code configuration} model
     * @param <C>                       the type of the configuration instance
     * @throws IllegalStateException if an instance with the same {@code configurationInstanceName} has already been registered
     */
    <C> void registerConfigurationInstance(Configuration configuration, String configurationInstanceName, C configurationInstance);

    /**
     * Provisions a {@link OperationExecutor} to execute the {@link Operation} modeled by {@code operation}
     * using the provided {@code configurationInstance}.
     * <p/>
     *
     * @param operation             the {@link Operation} model that the {@link OperationExecutor} is capable of implementing
     * @param configurationInstance an instance previously registered with {@link #registerConfigurationInstance(Configuration, String, Object)}
     * @param <C>                   the type of the configuration instance
     * @return a {@link OperationExecutor}
     * @throws IllegalStateException is {@code configurationInstance} has not been previously registered through {@link #registerConfigurationInstance(Configuration, String, Object)}
     */
    <C> OperationExecutor getOperationExecutor(Operation operation, C configurationInstance);

}
