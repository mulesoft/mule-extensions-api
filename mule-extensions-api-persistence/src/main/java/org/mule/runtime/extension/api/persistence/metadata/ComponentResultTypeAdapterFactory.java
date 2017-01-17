/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory}
 * 
 * @since 1.0
 */
final class ComponentResultTypeAdapterFactory
    implements TypeAdapterFactory {

  @Override
  public <C> TypeAdapter<C> create(Gson gson, TypeToken<C> type) {
    return type.getRawType().isAssignableFrom(ComponentMetadataResult.class)
        ? (TypeAdapter<C>) new ComponentResultTypeAdapter(gson) : null;
  }
}
