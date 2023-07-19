/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.runtime.extension.internal.persistence.ComponentMetadataResult;
import org.mule.runtime.extension.internal.persistence.ComponentResultTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory}
 * 
 * @since 1.0
 */
public final class ComponentResultTypeAdapterFactory
    implements TypeAdapterFactory {

  @Override
  public <C> TypeAdapter<C> create(Gson gson, TypeToken<C> type) {
    return type.getRawType().isAssignableFrom(ComponentMetadataResult.class)
        ? (TypeAdapter<C>) new ComponentResultTypeAdapter(gson)
        : null;
  }
}
