/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection;

/**
 * A model which can be augmented with custom key-value pairs of information
 * that are not part of the canonical introspection model. We call this pairs
 * {@code model properties}.
 * <p/>
 * This is useful for pieces of metadata which might not apply to all extensions
 * or might be specific to a particular implementations.
 * <p/>
 * Examples of a model property are the namespace URI and schema version for extensions
 * that support XML configuration, vendor specific information, custom policies, etc.
 * <p/>
 * The values added as model properties can be of any types, from simple types such as
 * {@link String}, {@link Long}, etc to complex types such as pojos or java beans. When
 * complex types are used, those should be immutable. This is because if a model definition
 * keeps changing the runtime behaviour could become inconsistent. It should also be thread-safe
 * since several threads should be able to query the model safely.
 *
 * @since 1.0
 */
public interface EnrichableModel
{

    /**
     * Returns a model property registered under {@code key}
     *
     * @param key the property's key
     * @param <T> the generic type of the return value
     * @return a model property or {@code null} if no such property is present on {@code this} model
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    <T> T getModelProperty(String key);
}
