/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension;

import org.mule.extension.introspection.Configuration;
import org.mule.extension.introspection.Extension;
import org.mule.extension.introspection.ExtensionFactory;
import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.declaration.Describer;
import org.mule.extension.runtime.ConfigurationInstanceProvider;

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
 * method in order for this manager to provision the underlying infrastructure to host and execute such configuration.
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
     * <li>The discovered describers are fed into a {@link ExtensionFactory} and transformed in {@link Extension} instances</li>
     * <li>Those extensions are registered through the {@link #registerExtension(Extension)} method</li>
     * </ul>
     * Finally, a {@link List} is returned with all the {@link Extension extensions} available after the discovery process finishes.
     *
     * @param classLoader a not {@code null} {@link java.lang.ClassLoader} in which to search for extensions
     * @return a {@link List} with all the available {@link Extension extensions}
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
     * Returns a {@link Set} listing all the discovered
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
    <C> void registerConfigurationInstanceProvider(Extension extension, String providerName, ConfigurationInstanceProvider<C> configurationInstanceProvider);
}
