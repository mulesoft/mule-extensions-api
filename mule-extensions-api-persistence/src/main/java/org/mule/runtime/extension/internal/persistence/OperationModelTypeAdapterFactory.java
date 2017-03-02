/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.operation.OperationModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapterFactory} which yields instances of {@link OperationModelTypeAdapter}
 *
 * @since 1.0
 */
public class OperationModelTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (OperationModel.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) new OperationModelTypeAdapter(this, gson);
    }

    return null;
  }
}
