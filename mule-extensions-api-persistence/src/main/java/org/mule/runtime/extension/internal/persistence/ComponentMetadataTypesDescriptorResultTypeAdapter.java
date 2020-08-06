/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataTypesDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * {@link TypeAdapter}
 *
 * @since 4.4
 */
public class ComponentMetadataTypesDescriptorResultTypeAdapter extends TypeAdapter<ComponentMetadataTypesDescriptorResult> {

  private static final String INPUT = "input";
  private static final String OUTPUT = "output";
  private static final String OUTPUT_ATTRIBUTES = "outputAttributes";
  private static final String FAILURES = "failures";
  private final Gson gson;

  public ComponentMetadataTypesDescriptorResultTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, ComponentMetadataTypesDescriptorResult result) throws IOException {
    out.beginObject();

    out.name(FAILURES);
    out.beginArray();
    result.getFailures().stream().forEach(f -> gson.toJson(f, new TypeToken<MetadataFailure>() {}.getType(), out));
    out.endArray();

    out.name(INPUT);
    if (result.getInput() != null && !result.getInput().isEmpty()) {
      out.beginObject();
      for (Map.Entry<String, MetadataType> entry : result.getInput().entrySet()) {
        out.name(entry.getKey());
        gson.toJson(entry.getValue(), new TypeToken<MetadataType>() {}.getType(), out);
      }
      out.endObject();
    } else {
      out.nullValue();
    }

    out.name(OUTPUT);
    if (result.getOutput() != null) {
      gson.toJson(result.getOutput(), new TypeToken<MetadataType>() {}.getType(), out);
    } else {
      out.nullValue();
    }

    out.name(OUTPUT_ATTRIBUTES);
    if (result.getOutputAttributes() != null) {
      gson.toJson(result.getOutputAttributes(), new TypeToken<MetadataType>() {}.getType(), out);
    } else {
      out.nullValue();
    }

    out.endObject();
  }

  @Override
  public ComponentMetadataTypesDescriptorResult read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    JsonArray failuresArray = json.get(FAILURES).getAsJsonArray();
    List<MetadataFailure> failures = new LinkedList<>();
    failuresArray.forEach(f -> failures.add(gson.fromJson(f, new TypeToken<MetadataFailure>() {}.getType())));

    if (!failures.isEmpty()) {
      return new ComponentMetadataTypesDescriptorResult(failure(failures));
    }
    Map<String, MetadataType> input = new HashMap<>();
    if (json.has(INPUT)) {
      JsonObject inputMapAsJsonObject = json.get(INPUT).getAsJsonObject();
      inputMapAsJsonObject.entrySet()
          .stream()
          .forEach(entry -> input.put(entry.getKey(),
                                      gson.fromJson(entry.getValue(), new TypeToken<MetadataType>() {}.getType())));
    }

    MetadataType output = null;
    if (json.has(OUTPUT)) {
      output = gson.fromJson(json.get(OUTPUT).getAsJsonObject(), new TypeToken<MetadataType>() {}.getType());
    }

    MetadataType outputAttributes = null;
    if (json.has(OUTPUT_ATTRIBUTES)) {
      outputAttributes = gson.fromJson(json.get(OUTPUT_ATTRIBUTES).getAsJsonObject(), new TypeToken<MetadataType>() {}.getType());
    }

    return new ComponentMetadataTypesDescriptorResult(success(new ComponentMetadataTypesDescriptor(input, output,
                                                                                                   outputAttributes)));
  }
}
