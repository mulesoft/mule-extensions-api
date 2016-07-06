/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.extension.api.runtime.operation.Interceptor;

/**
 * Creates new instances of {@link Interceptor}.
 * <p/>
 * Implementations are to be reusable and thread-safe
 *
 * @since 1.0
 */
@FunctionalInterface
public interface InterceptorFactory
{

    /**
     * Creates a new {@link Interceptor}
     *
     * @return a new {@link Interceptor}
     */
    Interceptor createInterceptor();
}
