/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory} implementation, which creates {@link TypeAdapter}s that for any subclass of {@link SuperClass},
 * fixes the serialization and desearilization to unique class {@link ImplementationClass}.
 * <p>
 * Does not matter which subclass of {@link SuperClass} is found, this factory will create {@link TypeAdapter}s of
 * {@link ImplementationClass} type.
 *
 * @since 1.0
 */
public final class DefaultImplementationTypeAdapterFactory<SuperClass, ImplementationClass extends SuperClass> implements TypeAdapterFactory
{

    private final Class<ImplementationClass> clazz;
    private final Class<SuperClass> superClass;

    /**
     * @param superClass class that will be used to look for implementations of it, at serialization and deserialization
     *                   time
     * @param clazz      class to fix the serialization or deserialization
     */
    public DefaultImplementationTypeAdapterFactory(Class<SuperClass> superClass, Class<ImplementationClass> clazz)
    {
        if (!superClass.isAssignableFrom(clazz))
        {
            throw new RuntimeException(String.format("[%s] class doesn't extends or implements [%s]", clazz, superClass));
        }
        this.clazz = clazz;
        this.superClass = superClass;
    }

    /**
     * @param gson                  The actual Gson serializer
     * @param type                  Implementation that GSON is trying to find a {@link TypeAdapter}
     * @param <ImplementationClass> type of objects that the {@link TypeAdapter} will create
     * @return if {@param type} is subclass of {@link #superClass} a {@link TypeAdapter}, that serializes and deserialize
     * {@link ImplementationClass} instances
     */
    @Override
    public <ImplementationClass> TypeAdapter<ImplementationClass> create(Gson gson, TypeToken<ImplementationClass> type)
    {
        if (superClass.isAssignableFrom(type.getRawType()))
        {
            return gson.getDelegateAdapter(this, TypeToken.get((Class<ImplementationClass>) clazz));
        }
        return null;
    }
}
