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
 * Contract for models capable of providing a {@link MetadataResolverFactory}
 *
 * @since 1.0
 */
public interface MetadataEnrichableModel
{

    /**
     * @return the {@link MetadataResolverFactory} required to instantiate the
     * {@link MetadataKeysResolver}, {@link MetadataContentResolver} and {@link MetadataOutputResolver}
     * associated to this component's model.
     */
    MetadataResolverFactory getMetadataResolverFactory();

}
