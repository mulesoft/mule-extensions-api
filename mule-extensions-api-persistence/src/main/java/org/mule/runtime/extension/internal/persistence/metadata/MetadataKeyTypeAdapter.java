/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.extension.api.metadata.NullMetadataKey;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MetadataKeyTypeAdapter extends TypeAdapter<MetadataKey> {

  private Gson gson = new Gson();

  @Override
  public void write(JsonWriter out, MetadataKey value) throws IOException {
    if (value instanceof NullMetadataKey) {
      gson.toJson(value, NullMetadataKey.class, out);
    } else if (value instanceof DefaultMetadataKey) {
      gson.toJson(value, DefaultMetadataKey.class, out);
    } else {
      throw new RuntimeException("Couldn't serialize MetadataKey for implementation: " + value.getClass());
    }
  }

  @Override
  public MetadataKey read(JsonReader in) throws IOException {
    JsonElement parse = new JsonParser().parse(in);
    JsonObject metadataKey = parse.getAsJsonObject();
    if (metadataKey.entrySet().isEmpty()) {
      return new NullMetadataKey();
    } else {
      return gson.fromJson(parse, DefaultMetadataKey.class);
    }
  }
}
