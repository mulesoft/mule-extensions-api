/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.ImmutableMetadataResult;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.internal.persistence.metadata.dto.ComponentMetadataResult;

import com.google.gson.reflect.TypeToken;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into
 * a readable and processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class MetadataDescriptorResultJsonSerializer extends AbstractMetadataResultJsonSerializer
{

    public MetadataDescriptorResultJsonSerializer()
    {
        super(false);
    }

    public MetadataDescriptorResultJsonSerializer(boolean prettyPrint)
    {
        super(prettyPrint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize(MetadataResult metadataResult)
    {
        return gson.toJson(new ComponentMetadataResult(metadataResult));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableMetadataResult<ImmutableComponentMetadataDescriptor> deserialize(String metadataResult)
    {
        ComponentMetadataResult result = gson.fromJson(metadataResult, new TypeToken<ComponentMetadataResult>(){}.getType());
        return (ImmutableMetadataResult<ImmutableComponentMetadataDescriptor>) result.toComponentMetadataResult();
    }

}
