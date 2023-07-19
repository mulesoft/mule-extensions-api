/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link OperationModel} and all its child interfaces
 *
 * @since 1.0
 */
public class OperationModelTypeAdapter extends KindEnrichedTypeAdapter<OperationModel> {

  private static final String OPERATION_KIND = "operation";

  public OperationModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(OperationModel value) {
    return OPERATION_KIND;
  }

  @Override
  protected TypeAdapter<OperationModel> getDelegateAdapter(String kind) {
    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get((Class) ImmutableOperationModel.class));
  }
}
