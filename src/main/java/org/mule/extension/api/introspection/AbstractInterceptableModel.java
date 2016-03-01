/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.runtime.InterceptorFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for implementations of {@link AbstractImmutableModel} which also implement the
 * {@link InterceptableModel} interface
 *
 * @since 1.0
 */
abstract class AbstractInterceptableModel extends AbstractImmutableModel implements InterceptableModel
{

    private final List<InterceptorFactory> interceptorFactories;

    /**
     * Creates a new instance
     *
     * @param name                 the model's name
     * @param description          the model's description
     * @param modelProperties      A {@link Map} of custom properties which extend this model
     * @param interceptorFactories a {@link List} with {@link InterceptorFactory} instances. Could be empty or even {@code null}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractInterceptableModel(String name, String description, Map<String, Object> modelProperties, List<InterceptorFactory> interceptorFactories)
    {
        super(name, description, modelProperties);
        this.interceptorFactories = interceptorFactories != null ? Collections.unmodifiableList(interceptorFactories) : Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<InterceptorFactory> getInterceptorFactories()
    {
        return interceptorFactories;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("AbstractInterceptableModel{")
                .append(super.toString())
                .append(", interceptorFactories=").append(interceptorFactories)
                .append('}').toString();
    }
}
