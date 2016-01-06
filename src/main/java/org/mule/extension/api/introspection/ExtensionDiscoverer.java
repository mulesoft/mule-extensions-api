/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.util.List;

/**
 * Interface used to discover a list of {@link ExtensionModel} objects from a {@link ClassLoader}
 * <p/>
 * since 1.0
 */
//TODO MULE-9267 Remove when ticket is defined and implemented
public interface ExtensionDiscoverer
{

    /**
     * Performs a search for extensions inside the provided classLoader.
     *
     * @param classLoader the {@link ClassLoader} on which the search will be performed
     * @return a {@link List} of {@link ExtensionModel}. Might be empty but it will never be {@code null}
     */
    List<ExtensionModel> discover(ClassLoader classLoader);

}
