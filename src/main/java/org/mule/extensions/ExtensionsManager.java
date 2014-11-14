/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions;

import org.mule.extensions.introspection.Extension;

import java.util.List;
import java.util.Set;

/**
 * Manages the {@link Extension}s available in the current context.
 * TODO: Define scopes and hierarchies. The extensions manager available in APP1 should see the extensions in the
 * runtime, the domain and the app. The one in APP2 should see the ones in runtime, domain and APP2. At the same time,
 * the one at a domain level should only see runtime and domain, and so forth...
 *
 * @since 1.0.0
 */
public interface ExtensionsManager
{

    /**
     * Scans the classpath visible to the given
     * {@link java.lang.ClassLoader} and registers them.
     * <p/>
     * The discovery process works as follows:
     * <ul>
     * <li>It scans the classpath looking for the files in the path META-INF/extensions/mule.extensions</li>
     * <li>The found files are assumed to be text files in which one canonical class name is found per line</li>
     * <li>Those classes are loaded and registered as {@link Extension}s</li>
     * </ul>
     * <p/>
     * If an {@link Extension} is found which is already registered,
     * then this manager will apply whatever policy finds more convenient to decide if the existing
     * registration is kept or replaced. That is up to the implementation. The only thing this contract
     * guarantees is that each extension type will be registered only once and that the discovery process
     * will not fail if many discovery runs are triggered over the same classpath.
     * <p/>
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
