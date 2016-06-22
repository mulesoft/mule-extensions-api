/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.runtime.InterceptorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for implementations of {@link BaseDeclaration} which can handle
 * {@link InterceptorFactory interceptor factories}
 *
 * @param <T> the concrete type for {@code this} declaration
 * @since 1.0
 */
public abstract class InterceptableDeclaration<T extends InterceptableDeclaration> extends NamedDeclaration<T>
{

    private final List<InterceptorFactory> interceptorFactories = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    InterceptableDeclaration(String name)
    {
        super(name);
    }

    /**
     * Adds the {@code interceptorFactory} to {@code this} declaration
     *
     * @param interceptorFactory a {@link InterceptorFactory}
     */
    public void addInterceptorFactory(InterceptorFactory interceptorFactory)
    {
        interceptorFactories.add(interceptorFactory);
    }

    /**
     * @return An immutable {@link List} with all the items added through {@link #addInterceptorFactory(InterceptorFactory)}
     */
    public List<InterceptorFactory> getInterceptorFactories()
    {
        return Collections.unmodifiableList(interceptorFactories);
    }
}
