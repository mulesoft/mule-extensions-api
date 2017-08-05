/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.model.construct.ImmutableConstructModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link OperationModel} and all its
 * child interfaces
 *
 * @since 1.0
 */
public class ConstructModelTypeAdapter extends KindEnrichedTypeAdapter<ConstructModel> {

  private static final String CONSTRUCT_KIND = "construct";

  public ConstructModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(ConstructModel value) {
    return CONSTRUCT_KIND;
  }

  @Override
  protected TypeAdapter<ConstructModel> getDelegateAdapter(String kind) {
    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get((Class) ImmutableConstructModel.class));
  }
}
