/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence;

import org.mule.extension.api.introspection.ModelProperty;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * Creates a {@link ModelPropertyMapTypeAdapter} when GSON requires a serialization of deserialization of
 * {@code Map<Class<? extends ModelProperty>, ModelProperty>} type.
 *
 * @since 1.0
 */
final class ModelPropertyMapTypeAdapterFactory implements TypeAdapterFactory
{

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        if (type.getType().equals(new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>()
        {
        }.getType()))
        {
            return (TypeAdapter<T>) new ModelPropertyMapTypeAdapter(gson);
        }
        return null;
    }
}
