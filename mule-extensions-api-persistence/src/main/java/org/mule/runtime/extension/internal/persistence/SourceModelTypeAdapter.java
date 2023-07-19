/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link SourceModel} and all its child interfaces
 *
 * @since 1.0
 */
public class SourceModelTypeAdapter extends KindEnrichedTypeAdapter<SourceModel> {

  public SourceModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(SourceModel value) {
    return "source";
  }

  @Override
  protected TypeAdapter<SourceModel> getDelegateAdapter(String kind) {
    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get((Class) ImmutableSourceModel.class));
  }
}
