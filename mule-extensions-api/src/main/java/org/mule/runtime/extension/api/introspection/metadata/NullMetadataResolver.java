/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.metadata;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;
import org.mule.metadata.java.JavaTypeLoader;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.MetadataContentResolver;
import org.mule.runtime.api.metadata.resolving.MetadataKeysResolver;
import org.mule.runtime.api.metadata.resolving.MetadataOutputResolver;
import org.mule.runtime.api.metadata.resolving.MetadataAttributesResolver;
import org.mule.runtime.extension.api.annotation.metadata.Content;

import java.util.Collections;
import java.util.Set;

/**
 * Null implementation of {@link MetadataContentResolver}, {@link MetadataAttributesResolver}
 * and {@link MetadataKeysResolver}, used to represent the absence of any of them when required.
 *
 * @since 1.0
 */
public final class NullMetadataResolver implements MetadataContentResolver<Object>, MetadataKeysResolver,
        MetadataOutputResolver<Object>, MetadataAttributesResolver<Object>
{

    /**
     * Null implementation of {@link MetadataKeysResolver}, used when no implementation
     * is provided by the connector developer. Represents the absence of a custom {@link MetadataKeysResolver},
     * returning an empty list of {@link MetadataKey}.
     *
     * @param context {@link MetadataContext} of the MetaData resolution
     * @return {@link Collections#emptyList()}
     * @throws MetadataResolvingException
     */
    @Override
    public Set<MetadataKey> getMetadataKeys(MetadataContext context) throws MetadataResolvingException
    {
        return Collections.emptySet();
    }

    /**
     * Null implementation of {@link MetadataContentResolver}, used when no implementation
     * is provided by the connector developer. Represents the absence of a custom {@link MetadataContentResolver},
     * returning a {@link NullType} instead of resolving a valid {@link MetadataType} for the {@link Content} parameter
     *
     * @param context {@link MetadataContext} of the MetaData resolution
     * @param key     {@link MetadataKey} of the type which's structure has to be resolved
     * @return {@link NullType}
     * @throws MetadataResolvingException
     */
    @Override
    public MetadataType getContentMetadata(MetadataContext context, Object key) throws MetadataResolvingException
    {
        return BaseTypeBuilder.create(JavaTypeLoader.JAVA).nullType().build();
    }

    /**
     * Null implementation of {@link MetadataOutputResolver}, used when no implementation
     * is provided by the connector developer. Represents the absence of a custom {@link MetadataOutputResolver},
     * returning a {@link NullType} instead of resolving a dynamic {@link MetadataType} for the component's output.
     *
     * @param context {@link MetadataContext} of the MetaData resolution
     * @param key     {@link MetadataKey} of the type which's structure has to be resolved
     * @return {@link NullType}
     * @throws MetadataResolvingException
     */
    @Override
    public MetadataType getOutputMetadata(MetadataContext context, Object key) throws MetadataResolvingException
    {
        return BaseTypeBuilder.create(JavaTypeLoader.JAVA).nullType().build();
    }

    /**
     * Null implementation of {@link MetadataAttributesResolver}, used when no implementation
     * is provided by the connector developer. Represents the absence of a custom {@link MetadataAttributesResolver},
     * returning a {@link AnyType} instead of resolving a dynamic {@link MetadataType} for the component's output attributes.
     *
     * @param context {@link MetadataContext} of the MetaData resolution
     * @param key     {@link MetadataKey} of the type which's structure has to be resolved
     * @return {@link NullType}
     * @throws MetadataResolvingException
     */
    @Override
    public MetadataType getAttributesMetadata(MetadataContext context, Object key) throws MetadataResolvingException
    {
        return BaseTypeBuilder.create(JavaTypeLoader.JAVA).anyType().build();
    }
}
