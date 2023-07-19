/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.api.util.HierarchyClassMap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Creates a {@link ModelPropertyMapTypeAdapter} when GSON requires a serialization of deserialization of
 * {@code Map<Class<? extends ModelProperty>, ModelProperty>} type.
 *
 * @since 1.0
 */
public final class ModelPropertyMapTypeAdapterFactory implements TypeAdapterFactory {

  private final Type mapType = new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>() {}.getType();
  private final Type hierarchyClassMapType = new TypeToken<HierarchyClassMap<ModelProperty>>() {}.getType();

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getType().equals(mapType) || type.getType().equals(hierarchyClassMapType)) {
      return (TypeAdapter<T>) new ModelPropertyMapTypeAdapter(gson);
    }
    return null;
  }
}
