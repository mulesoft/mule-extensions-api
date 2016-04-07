/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A Gson serializer/deserializer which can handle {@link Class} instances
 * by relying on their qualified name
 *
 * @since 1.0
 */
final class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>>
{

    @Override
    public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        try
        {
            return Class.forName(json.getAsString());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}