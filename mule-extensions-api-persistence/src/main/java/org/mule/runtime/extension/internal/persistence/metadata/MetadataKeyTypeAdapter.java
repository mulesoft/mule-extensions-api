/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeyBuilder;
import org.mule.runtime.extension.api.metadata.NullMetadataKey;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MetadataKeyTypeAdapter extends TypeAdapter<MetadataKey> {

  private Gson gson = new GsonBuilder().create();

  @Override
  public void write(JsonWriter out, MetadataKey value) throws IOException {
    if (value != null) {
      if (value instanceof NullMetadataKey) {
        gson.toJson(value, NullMetadataKey.class, out);
      } else if (value instanceof DefaultMetadataKey) {
        gson.toJson(value, DefaultMetadataKey.class, out);
      } else {
        throw new RuntimeException("Couldn't serialize MetadataKey for implementation: " + value.getClass());
      }
    } else {
      out.nullValue();
    }
  }

  @Override
  public MetadataKey read(JsonReader in) throws IOException {
    JsonElement jsonElement = new JsonParser().parse(in);
    return toMetadataKey(jsonElement);
  }

  private MetadataKey toMetadataKey(JsonElement jsonElement) {
    if (jsonElement.isJsonNull()) {
      return null;
    }
    JsonObject metadataKey = jsonElement.getAsJsonObject();

    if (metadataKey.entrySet().isEmpty()) {
      return new NullMetadataKey();
    }

    JsonElement id = metadataKey.get("id");
    JsonElement displayName = metadataKey.get("displayName");
    JsonElement partName = metadataKey.get("partName");
    JsonElement childs = metadataKey.get("childs");

    if (id != null && displayName != null && partName != null && childs != null) {
      MetadataKeyBuilder key = MetadataKeyBuilder.newKey(id.getAsString())
          .withDisplayName(displayName.getAsString())
          .withPartName(partName.getAsString());

      childs.getAsJsonArray()
          .forEach(child -> key.withChild(toMetadataKey(child)));

      return key.build();
    }

    return null;
  }
}
