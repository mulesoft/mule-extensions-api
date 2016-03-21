/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.metadata.MetadataResolverFactory;

/**
 * A contract interface for an object that has a {@link MetadataResolverFactory}
 * associated for Metadata retrieval
 *
 * @param <D> the generic type of the target {@link Descriptor}
 * @since 1.0
 */
interface HasMetadataResolver<D extends Descriptor>
{
    /**
     *
     * @param metadataResolverFactory the {@link MetadataResolverFactory} associated to the enriched component
     * @return a {@link Descriptor} enriched with a {@link MetadataResolverFactory} instance
     */
    D withMetadataResolverFactory(MetadataResolverFactory metadataResolverFactory);

}
