/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.ExtensionManager;

import java.util.List;

/**
 * A component capable of searching the classpath for extensions
 * according to the algorithm described in {@link ExtensionManager#discoverExtensions(ClassLoader)}
 *
 * @since 1.0
 */
public interface ExtensionDiscoverer
{

    /**
     * Performs a search for extensions according to the algorithm described in
     * {@link ExtensionManager#discoverExtensions(ClassLoader)}
     *
     * @param classLoader the {@link ClassLoader} on which the search will be performed
     * @return a {@link List} of {@link RuntimeExtensionModel}. Might be empty but it will never be {@code null}
     */
    List<RuntimeExtensionModel> discover(ClassLoader classLoader);
}
