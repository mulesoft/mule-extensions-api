/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ModelProperty;

/**
 * A contract interface for an object capable of adding a model property
 * into a {@link Descriptor}
 *
 * @param <T> the generic type of the target {@link Descriptor}
 * @since 1.0
 */
public interface HasModelProperties<T extends Descriptor>
{

    /**
     * Adds the given {@code modelProperty}
     *
     * @param modelProperty a {@link ModelProperty}
     * @return {@code this} descriptor
     * @throws IllegalArgumentException if {@code modelProperty} is {@code null{}}
     */
    T withModelProperty(ModelProperty modelProperty);

}
