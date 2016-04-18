/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.runtime.extension.api.runtime.Interceptor;

import java.util.List;

/**
 * A component which can be intercepted through a {@link List} of
 * {@link Interceptor interceptors} that it provides.
 * <p/>
 * Notice that the interceptors are returned in a {@link List}, which
 * carries the concept of order. Although implementations are to return
 * the interceptors in the order they would like them to be executed,
 * the runtime reserves the right to not only re-arrange them but to also
 * insert other interceptors before, after and in-between of them.
 * <p/>
 * At the same time, just like the runtime doesn't guarantee to respect
 * the given order, it doesn't commit to make a best effort to alter them
 * as little as possible or convenient
 *
 * @since 1.0
 */
public interface Interceptable
{

    /**
     * Provides a list of {@link Interceptor interceptors} in a proposed
     * order of execution
     *
     * @return a immutable {@link List}. Might be empty but will never be {@code null}
     */
    List<Interceptor> getInterceptors();
}
