/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence;

import org.mule.extension.api.introspection.EnrichableModel;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.property.DisplayModelProperty;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link TypeAdapter} implementation that knows how to serialize and deserialize {@code Map<Class<? extends ModelProperty>, ModelProperty>}.
 * This {@link TypeAdapter} has been created at this level, and not just a {@link TypeAdapter<ModelProperty>}, to be able
 * to easily tag every object with a representative name of the class.
 * <p>
 * Due to the nature of {@link ModelProperty}, that can be dynamically attached to any {@link EnrichableModel}, only
 * the already know set of {@link ModelProperty} will be tagged with a friendly name, example: {@link DisplayModelProperty}
 * is going to be identified with the {@code display} name. Otherwise, the {@link ModelProperty} will be serialized
 * tagging it with the full qualifier name of the class.
 * <p>
 * When deserializing a {@link ModelProperty}s, their full qualified name will be used, if the class is not found in the
 * ClassLoader the {@link ModelProperty} object will be discarded
 *
 * @since 1.0
 */
final class ModelPropertyMapTypeAdapter extends TypeAdapter<Map<Class<? extends ModelProperty>, ModelProperty>>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelPropertyMapTypeAdapter.class);
    private static final Map<Class<? extends ModelProperty>, String> classNameMapping = JsonSerializationConstants.getClassNameMapping();
    private static final Map<String, Class<? extends ModelProperty>> nameClassMapping = JsonSerializationConstants.getNameClassMapping();

    private final Gson gson;

    ModelPropertyMapTypeAdapter(Gson gson)
    {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Map<Class<? extends ModelProperty>, ModelProperty> modelPropertyMap) throws IOException
    {
        out.beginObject();
        for (Map.Entry<Class<? extends ModelProperty>, ModelProperty> entry : modelPropertyMap.entrySet())
        {
            final ModelProperty modelProperty = entry.getValue();
            final Class<? extends ModelProperty> modelPropertyClass = entry.getKey();

            if (modelProperty.isExternalizable())
            {
                out.name(getSerializableModelPropertyName(modelPropertyClass));
                final TypeAdapter adapter = gson.getAdapter(modelPropertyClass);
                adapter.write(out, modelProperty);
            }
        }
        out.endObject();

    }

    @Override
    public Map<Class<? extends ModelProperty>, ModelProperty> read(JsonReader in) throws IOException
    {
        final Map<Class<? extends ModelProperty>, ModelProperty> modelPropertyHashMap = new HashMap<>();

        in.beginObject();
        while (in.hasNext())
        {
            final Optional<Class<? extends ModelProperty>> modelPropertyClass = getClassForModelProperty(in.nextName());

            if (modelPropertyClass.isPresent())
            {
                final Class<? extends ModelProperty> type = modelPropertyClass.get();
                final TypeAdapter<?> adapter = gson.getAdapter(type);
                final ModelProperty read = (ModelProperty) adapter.read(in);
                modelPropertyHashMap.put(type, read);
            }
        }
        in.endObject();
        return modelPropertyHashMap;
    }

    private Optional<Class<? extends ModelProperty>> getClassForModelProperty(String modelPropertyName)
    {
        Class<? extends ModelProperty> modelPropertyClass = null;
        if (nameClassMapping.containsKey(modelPropertyName))
        {
            modelPropertyClass = nameClassMapping.get(modelPropertyName);
        }
        else
        {
            try
            {
                modelPropertyClass = (Class<? extends ModelProperty>) Class.forName(modelPropertyName);
            }
            catch (ClassNotFoundException e)
            {
                throw new ExtensionModelSerializationException(String.format("Error loading [%s] ModelProperty. Class not found in the current classloader", modelPropertyName), e);
            }
        }

        return Optional.ofNullable(modelPropertyClass);
    }


    private String getSerializableModelPropertyName(Class<? extends ModelProperty> modelPropertyClass)
    {
        return classNameMapping.getOrDefault(modelPropertyClass, modelPropertyClass.getName());
    }
}
