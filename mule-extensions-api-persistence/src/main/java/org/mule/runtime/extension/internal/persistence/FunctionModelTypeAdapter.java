/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.extension.api.model.function.ImmutableFunctionModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link FunctionModel} and all its child interfaces
 *
 * @since 1.0
 */
public class FunctionModelTypeAdapter extends KindEnrichedTypeAdapter<FunctionModel> {

  public FunctionModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(FunctionModel value) {
    return "function";
  }

  @Override
  protected TypeAdapter<FunctionModel> getDelegateAdapter(String kind) {
    Class functionModelClass = ImmutableFunctionModel.class;
    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(functionModelClass));
  }
}
