/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.runtime.InterceptorFactory;

/**
 * A contract interface for an object capable of adding an {@link InterceptorFactory}
 * into a {@link Descriptor}
 *
 * @param <T> the generic type of the target {@link Descriptor}
 * @since 1.0
 */
public interface HasInterceptors<T extends Descriptor>
{

    /**
     * Adds the given {@code interceptorFactory}
     *
     * @param interceptorFactory a {@link InterceptorFactory}
     * @return {@code this} descriptor
     */
    T withInterceptorFrom(InterceptorFactory interceptorFactory);
}
