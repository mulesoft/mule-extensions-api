/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantType;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapterFactory} which produces type adapters capable of serializing the {@link OAuthGrantType} hierarchy
 *
 * @since 1.2.1
 */
public class OAuthGrantTypeTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (OAuthGrantType.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) new OAuthGrantTypeTypeAdapter();
    }

    return null;
  }
}
