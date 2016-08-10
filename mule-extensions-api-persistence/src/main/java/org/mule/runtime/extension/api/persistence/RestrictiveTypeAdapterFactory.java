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
 * {@link TypeAdapterFactory} implementation, which creates {@link TypeAdapter}s that only for a class of type {@link SpecificClass},
 * fixes the serialization and deserialization to a unique class {@link ImplementationClass}.
 *
 * @since 1.0
 */
public final class RestrictiveTypeAdapterFactory<SpecificClass, ImplementationClass extends SpecificClass>
    implements TypeAdapterFactory {

  private final Class<ImplementationClass> implementationClazz;
  private final Class<SpecificClass> clazz;

  /**
   * @param specificClass class to look for at serialization and deserialization time.
   * @param clazz         class to fix the serialization or deserialization
   */
  public RestrictiveTypeAdapterFactory(Class<SpecificClass> specificClass, Class<ImplementationClass> clazz) {
    this.implementationClazz = clazz;
    this.clazz = specificClass;
  }

  /**
   * @param gson                  The actual Gson serializer
   * @param type                  Implementation that GSON is trying to find a {@link TypeAdapter}
   * @param <ImplementationClass> type of objects that the {@link TypeAdapter} will create
   * @return if {@param type} is a {@link #clazz} a {@link TypeAdapter}, that serializes and deserialize
   * {@link ImplementationClass} instances
   */
  @Override
  public <ImplementationClass> TypeAdapter<ImplementationClass> create(Gson gson, TypeToken<ImplementationClass> type) {
    if (clazz.equals(type.getRawType())) {
      return gson.getDelegateAdapter(this, TypeToken.get((Class<ImplementationClass>) implementationClazz));
    }
    return null;
  }
}
