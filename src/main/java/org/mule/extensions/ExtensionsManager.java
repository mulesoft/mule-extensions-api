/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions;

import org.mule.extensions.introspection.Describer;
import org.mule.extensions.introspection.Extension;
import org.mule.extensions.introspection.ExtensionFactory;

import java.util.List;
import java.util.Set;

/**
 * Manages the {@link Extension}s available in the current context.
 * TODO: Define scopes and hierarchies. The extensions manager available in APP1 should see the extensions in the
 * runtime, the domain and the app. The one in APP2 should see the ones in runtime, domain and APP2. At the same time,
 * the one at a domain level should only see runtime and domain, and so forth...
 *
 * @since 1.0
 */
public interface ExtensionsManager
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
     * Returns a {@link java.util.Set} listing all the available
     * {@link Extension}s.
     *
     * @return an {@link java.util.Set}. Will not be {@code null} but might be empty
     */
    Set<Extension> getExtensions();

    /**
     * Returns a {@link java.util.Set} with the {@link Extension}s
     * that have the given capability
     *
     * @param capability a {@link java.lang.Class} representing a capability
     * @return a {@link java.util.Set} with the matching {@link Extension}s. It might
     * be {@code null} but will never be empty
     */
    Set<Extension> getExtensionsCapableOf(Class<?> capability);

}
