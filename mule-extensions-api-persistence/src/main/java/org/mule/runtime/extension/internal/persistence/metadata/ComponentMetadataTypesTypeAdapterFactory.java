/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.runtime.extension.internal.persistence.ComponentMetadataTypesDescriptorResult;
import org.mule.runtime.extension.internal.persistence.ComponentMetadataTypesDescriptorResultTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory}
 * 
 * @since 1.4
 */
public final class ComponentMetadataTypesTypeAdapterFactory
    implements TypeAdapterFactory {

  @Override
  public <C> TypeAdapter<C> create(Gson gson, TypeToken<C> type) {
    return type.getRawType().isAssignableFrom(ComponentMetadataTypesDescriptorResult.class)
        ? (TypeAdapter<C>) new ComponentMetadataTypesDescriptorResultTypeAdapter(gson)
        : null;
  }
}
