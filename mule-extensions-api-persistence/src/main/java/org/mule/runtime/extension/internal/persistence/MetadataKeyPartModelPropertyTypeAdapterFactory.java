/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.extension.api.property.MetadataKeyPartModelProperty;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Creates a {@link MetadataKeyPartModelPropertyTypeAdapter} to be used when GSON requires serialization/deserialization of
 * {@link MetadataKeyPartModelProperty}.
 * <p>
 * The reason for having this factory is to provide the adapter with the default adapter for delegation.
 *
 * @since 1.8
 */
public final class MetadataKeyPartModelPropertyTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getType().equals(MetadataKeyPartModelProperty.class)) {
      TypeAdapter<MetadataKeyPartModelProperty> delegateAdapter =
          gson.getDelegateAdapter(this, TypeToken.get(MetadataKeyPartModelProperty.class));
      return (TypeAdapter<T>) new MetadataKeyPartModelPropertyTypeAdapter(delegateAdapter);
    }
    return null;
  }
}
