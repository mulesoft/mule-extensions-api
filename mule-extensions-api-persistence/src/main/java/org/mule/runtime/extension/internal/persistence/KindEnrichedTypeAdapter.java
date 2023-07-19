/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A {@link TypeAdapter} that serializes a model adding a "kind" property which specifies the kind of model being serialized
 *
 * @since 1.0
 */
abstract class KindEnrichedTypeAdapter<T> extends TypeAdapter<T> {

  protected static final String KIND = "kind";

  protected final TypeAdapterFactory typeAdapterFactory;
  protected final Gson gson;

  public KindEnrichedTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    this.typeAdapterFactory = typeAdapterFactory;
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, T value) throws IOException {
    String kind = getKind(value);
    JsonObject json = getDelegateAdapter(kind).toJsonTree(value).getAsJsonObject();
    json.addProperty(KIND, kind);
    gson.toJson(json, out);
  }

  protected abstract String getKind(T value);

  @Override
  public T read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    if (!json.has(KIND)) {
      throw new IllegalArgumentException("Invalid json. Property kind wasn't specified");
    }

    String kind = json.get(KIND).getAsString();
    return getDelegateAdapter(kind).fromJsonTree(json);
  }

  /**
   * Returns a {@link TypeAdapter} parameterized with the {@link Class} from a {@link String} with the kind property
   */
  protected abstract TypeAdapter<T> getDelegateAdapter(String kind);
}
