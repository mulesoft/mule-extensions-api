/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.metadata;

import org.mule.runtime.api.metadata.resolving.MetadataContentResolver;
import org.mule.runtime.api.metadata.resolving.MetadataKeysResolver;
import org.mule.runtime.api.metadata.resolving.MetadataOutputResolver;

/**
 * Provides instances of the {@link MetadataKeysResolver}, {@link MetadataKeysResolver}
 * and {@link MetadataOutputResolver} resolving types associated to a Component
 *
 * @since 1.0
 */
public interface MetadataResolverFactory
{

    /**
     * Provides an instance of the {@link MetadataKeysResolver} type associated to the Component
     *
     * @return an instance of the {@link MetadataKeysResolver}
     */
    MetadataKeysResolver getKeyResolver();

    /**
     * Provides an instance of the {@link MetadataContentResolver} type associated to the Component
     *
     * @return an instance of the {@link MetadataContentResolver}
     */
    <T> MetadataContentResolver<T> getContentResolver();

    /**
     * Provides an instance of the {@link MetadataOutputResolver} type associated to the Component
     *
     * @return an instance of the {@link MetadataOutputResolver}
     */
    <T> MetadataOutputResolver<T> getOutputResolver();

}
