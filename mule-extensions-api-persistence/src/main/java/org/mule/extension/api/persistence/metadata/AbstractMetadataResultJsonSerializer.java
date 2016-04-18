/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence.metadata;

import org.mule.runtime.api.metadata.descriptor.ImmutableOutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.extension.api.persistence.DefaultImplementationTypeAdapterFactory;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Abstract implementation of a serializer that can convert a {@link MetadataResult} of some payload type into
 * a readable and processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public abstract class AbstractMetadataResultJsonSerializer
{

    protected final Gson gson;

    public AbstractMetadataResultJsonSerializer(boolean prettyPrint)
    {
        final GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(TypeMetadataDescriptor.class, ImmutableTypeMetadataDescriptor.class))
                .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(OutputMetadataDescriptor.class, ImmutableOutputMetadataDescriptor.class))
                .registerTypeAdapter(MetadataType.class, new MetadataTypeGsonTypeAdapter());

        if (prettyPrint)
        {
            gsonBuilder.setPrettyPrinting();
        }

        this.gson = gsonBuilder.create();
    }

    /**
     * @param result the {@link MetadataResult} to be serialized
     * @return {@link String} JSON representation of the {@link MetadataResult}
     */
    public abstract String serialize(MetadataResult result);

    /**
     * Deserializes a JSON representation of an {@link MetadataResult}, to an actual instance of it.
     *
     * @param result the serialized {@link MetadataResult} in a {@link String} JSON representation
     * @return an instance of {@link MetadataResult} based on the serialized JSON
     */
    public abstract MetadataResult deserialize(String result);
}
