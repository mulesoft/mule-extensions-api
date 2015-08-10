/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

/**
 * A contract interface for an object capable of registering capabilities
 * in behalf of a {@link Descriptor}
 *
 * @param <T> the type of {@link Descriptor} for which capabilities are being received
 * @since 1.0
 */
public interface HasCapabilities<T extends Descriptor>
{

    /**
     * Adds the given {@code capability}
     *
     * @param capability a capability
     * @return {@value this}
     */
    T withCapability(Object capability);


}
