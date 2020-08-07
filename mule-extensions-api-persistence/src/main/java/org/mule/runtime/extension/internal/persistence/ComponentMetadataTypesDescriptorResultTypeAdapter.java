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
 * @since 1.4
 */
public class ComponentMetadataTypesDescriptorResultTypeAdapter extends TypeAdapter<ComponentMetadataTypesDescriptorResult> {

  private static final String INPUT_METADATA = "inputMetadata";
  private static final String OUTPUT_METADATA = "outputMetadata";
  private static final String OUTPUT_ATTRIBUTES_METADATA = "outputAttributesMetadata";
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

    out.name(INPUT_METADATA);
    if (result.getInputMetadata() != null && !result.getInputMetadata().isEmpty()) {
      out.beginObject();
      for (Map.Entry<String, MetadataType> entry : result.getInputMetadata().entrySet()) {
        out.name(entry.getKey());
        gson.toJson(entry.getValue(), new TypeToken<MetadataType>() {}.getType(), out);
      }
      out.endObject();
    } else {
      out.nullValue();
    }

    out.name(OUTPUT_METADATA);
    if (result.getOutputMetadata() != null) {
      gson.toJson(result.getOutputMetadata(), new TypeToken<MetadataType>() {}.getType(), out);
    } else {
      out.nullValue();
    }

    out.name(OUTPUT_ATTRIBUTES_METADATA);
    if (result.getOutputAttributesMetadata() != null) {
      gson.toJson(result.getOutputAttributesMetadata(), new TypeToken<MetadataType>() {}.getType(), out);
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
    Map<String, MetadataType> inputMetadata = new HashMap<>();
    if (json.has(INPUT_METADATA)) {
      JsonObject inputMapAsJsonObject = json.get(INPUT_METADATA).getAsJsonObject();
      inputMapAsJsonObject.entrySet()
          .stream()
          .forEach(entry -> inputMetadata.put(entry.getKey(),
                                              gson.fromJson(entry.getValue(), new TypeToken<MetadataType>() {}.getType())));
    }

    MetadataType outputMetadata = null;
    if (json.has(OUTPUT_METADATA)) {
      outputMetadata = gson.fromJson(json.get(OUTPUT_METADATA).getAsJsonObject(), new TypeToken<MetadataType>() {}.getType());
    }

    MetadataType outputAttributesMetadata = null;
    if (json.has(OUTPUT_ATTRIBUTES_METADATA)) {
      outputAttributesMetadata =
          gson.fromJson(json.get(OUTPUT_ATTRIBUTES_METADATA).getAsJsonObject(), new TypeToken<MetadataType>() {}.getType());
    }

    return new ComponentMetadataTypesDescriptorResult(success(new ComponentMetadataTypesDescriptor(inputMetadata, outputMetadata,
                                                                                                   outputAttributesMetadata)));
  }
}
