/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory} implementation, which creates {@link TypeAdapter}s that for any subclass of {@link S}, fixes the
 * serialization and deserialization to unique class {@link T}.
 * <p>
 * Does not matter which subclass of {@link S} is found, this factory will create {@link TypeAdapter}s of {@link T} type.
 *
 * @since 1.0
 */
public final class DefaultImplementationTypeAdapterFactory<S, T extends S> implements TypeAdapterFactory {

  private final Class<T> clazz;
  private final Class<S> superClass;

  /**
   * @param superClass class that will be used to look for implementations of it, at serialization and deserialization time
   * @param clazz      class to fix the serialization or deserialization
   */
  public DefaultImplementationTypeAdapterFactory(Class<S> superClass, Class<T> clazz) {
    if (!superClass.isAssignableFrom(clazz)) {
      throw new RuntimeException(String.format("[%s] class doesn't extends or implements [%s]", clazz, superClass));
    }
    this.clazz = clazz;
    this.superClass = superClass;
  }

  /**
   * @param gson The actual {@link Gson} serializer
   * @param type Implementation that {@link Gson} is trying to find a {@link TypeAdapter}
   * @param <C>  type of objects that the {@link TypeAdapter} will create
   * @return if {@param type} is subclass of {@link #superClass} a {@link TypeAdapter}, that serializes and deserialize {@link C}
   *         instances
   */
  @Override
  public <C> TypeAdapter<C> create(Gson gson, TypeToken<C> type) {
    if (superClass.isAssignableFrom(type.getRawType())) {
      return gson.getDelegateAdapter(this, TypeToken.get((Class<C>) clazz));
    }
    return null;
  }
}
