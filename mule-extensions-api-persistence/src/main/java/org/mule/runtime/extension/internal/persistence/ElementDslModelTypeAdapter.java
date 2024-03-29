/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.ParameterDslConfiguration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A {@link TypeAdapter} for serializing instances of {@link ParameterDslConfiguration}
 *
 * @since 1.0
 */
public class ElementDslModelTypeAdapter extends TypeAdapter<ParameterDslConfiguration> {

  private static final String ALLOW_TOP_LEVEL_DEFINITION = "allowTopLevelDefinition";
  private static final String ALLOWS_REFERENCES = "allowsReferences";
  private static final String ALLOWS_INLINE_DEFINITION = "allowsInlineDefinition";

  @Override
  public void write(JsonWriter out, ParameterDslConfiguration value) throws IOException {
    out.beginObject();
    out.name(ALLOWS_INLINE_DEFINITION).value(value.allowsInlineDefinition());
    out.name(ALLOWS_REFERENCES).value(value.allowsReferences());
    out.name(ALLOW_TOP_LEVEL_DEFINITION).value(value.allowTopLevelDefinition());
    out.endObject();
  }

  @Override
  public ParameterDslConfiguration read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return ParameterDslConfiguration.builder()
        .allowsInlineDefinition(read(json, ALLOWS_INLINE_DEFINITION))
        .allowsReferences(read(json, ALLOWS_REFERENCES))
        .allowTopLevelDefinition(read(json, ALLOW_TOP_LEVEL_DEFINITION))
        .build();
  }

  private boolean read(JsonObject json, String memberName) {
    return json.get(memberName).getAsBoolean();
  }
}
