/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.runtime.InterceptorFactory;

/**
 * A contract interface for a declarer capable of adding an {@link InterceptorFactory}
 *
 * @param <T> the type of the implementing type. Used to allow method chaining
 * @since 1.0
 */
public interface HasInterceptors<T>
{

    /**
     * Adds the given {@code interceptorFactory}
     *
     * @param interceptorFactory a {@link InterceptorFactory}
     * @return {@code this} declarer
     */
    T withInterceptorFrom(InterceptorFactory interceptorFactory);
}
