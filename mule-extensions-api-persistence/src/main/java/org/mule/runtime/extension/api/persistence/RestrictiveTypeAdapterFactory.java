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
 * {@link TypeAdapterFactory} implementation, which creates {@link TypeAdapter}s that only for a class of type {@link S},
 * fixes the serialization and deserialization to a unique class {@link I}.
 *
 * @since 1.0
 */
public final class RestrictiveTypeAdapterFactory<S, I extends S>
    implements TypeAdapterFactory {

  private final Class<I> implementationClazz;
  private final Class<S> clazz;

  /**
   * @param specificClass class to look for at serialization and deserialization time.
   * @param clazz         class to fix the serialization or deserialization
   */
  public RestrictiveTypeAdapterFactory(Class<S> specificClass, Class<I> clazz) {
    this.implementationClazz = clazz;
    this.clazz = specificClass;
  }

  /**
   * @param gson The actual Gson serializer
   * @param type Implementation that GSON is trying to find a {@link TypeAdapter}
   * @param <T>  type of objects that the {@link TypeAdapter} will create
   * @return if {@param type} is a {@link #clazz} a {@link TypeAdapter}, that serializes and deserialize
   * {@link T} instances
   */
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (clazz.equals(type.getRawType())) {
      return gson.getDelegateAdapter(this, TypeToken.get((Class<T>) implementationClazz));
    }
    return null;
  }
}
