/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.function.FunctionModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapterFactory} which yields instances of {@link FunctionModelTypeAdapter}
 *
 * @since 1.0
 */
public class FunctionModelTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (FunctionModel.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) new FunctionModelTypeAdapter(this, gson);
    }

    return null;
  }
}
