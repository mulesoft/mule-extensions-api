/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.value;

import org.mule.runtime.api.value.ImmutableValueResult;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.api.value.ValueResult;
import org.mule.runtime.extension.api.values.ImmutableValue;
import org.mule.runtime.extension.internal.persistence.DefaultImplementationTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Serializer that can convert a {@link ValueResult} type into a readable and processable JSON representation and from a JSON
 * {@link String} to an {@link ValueResult} instance
 *
 * @since 1.0
 */
public final class ValueResultJsonSerializer {

  private final Gson gson;

  public ValueResultJsonSerializer() {
    this(false);
  }

  public ValueResultJsonSerializer(boolean prettyPrinting) {
    GsonBuilder gsonBuilder = new GsonBuilder();

    if (prettyPrinting) {
      gsonBuilder.setPrettyPrinting();
    }

    gson = gsonBuilder
        .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(ValueResult.class, ImmutableValueResult.class))
        .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(Value.class, ImmutableValue.class))
        .create();
  }

  /**
   * Serializes to a JSON representation a {@link ValueResult}
   *
   * @param valueResult {@link ValueResult result} object to serialize
   * @return The JSON representation of the given {@link ValueResult}
   */
  public String serialize(ValueResult valueResult) {
    return gson.toJson(valueResult);
  }

  /**
   * Deserializes a JSON representing a {@link ValueResult} to an actual {@link ValueResult} instance.
   *
   * @param valueResult JSON representing a {@link ValueResult result}
   * @return A {@link ValueResult} instance
   */
  public ValueResult deserialize(String valueResult) {
    return gson.fromJson(valueResult, ImmutableValueResult.class);
  }
}
