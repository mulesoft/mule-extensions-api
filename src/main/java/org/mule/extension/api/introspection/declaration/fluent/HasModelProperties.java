/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

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
     * Adds a model property under the given {@code key} and {@code value}
     *
     * @param key   the property's key
     * @param value the property's value
     * @return {@code this} descriptor
     */
    T withModelProperty(String key, Object value);

}
