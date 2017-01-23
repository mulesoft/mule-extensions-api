/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import static org.mule.runtime.extension.internal.persistence.ComponentMetadataResult.COMPONENT;
import static org.mule.runtime.extension.internal.persistence.ComponentMetadataResult.OPERATION;
import static org.mule.runtime.extension.internal.persistence.ComponentMetadataResult.SOURCE;
import static org.mule.runtime.extension.internal.persistence.ComponentMetadataResult.TYPE;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.metadata.MetadataAttributes;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
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
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link TypeAdapter} to handle {@link ComponentMetadataResult} instances
 *
 * @since 1.0
 */
public class ComponentResultTypeAdapter extends TypeAdapter<ComponentMetadataResult> {

  private static final String METADATA_ATTRIBUTES = "metadataAttributes";
  private static final String FAILURES = "failures";
  private final Gson gson;

  public ComponentResultTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, ComponentMetadataResult result) throws IOException {
    out.beginObject();

    out.name(FAILURES);
    out.beginArray();
    result.getFailures().stream().forEach(f -> gson.toJson(f, new TypeToken<MetadataFailure>() {}.getType(), out));
    out.endArray();

    out.name(METADATA_ATTRIBUTES);
    gson.toJson(result.getMetadataAttributes(), new TypeToken<MetadataAttributes>() {}.getType(), out);

    if (result.isOperation()) {
      out.name(TYPE).value(OPERATION);
      out.name(COMPONENT);
      gson.toJson(result.getModel(),
                  new TypeToken<OperationModel>() {}.getType(),
                  out);

    } else if (result.isSource()) {
      out.name(TYPE).value(SOURCE);
      out.name(COMPONENT);
      gson.toJson(result.getModel(),
                  new TypeToken<SourceModel>() {}.getType(), out);
    }

    out.endObject();
  }

  @Override
  public ComponentMetadataResult read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    JsonArray failuresArray = json.get(FAILURES).getAsJsonArray();
    List<MetadataFailure> failures = new LinkedList<>();
    failuresArray.forEach(f -> failures.add(gson.fromJson(f, new TypeToken<MetadataFailure>() {}.getType())));

    if (!failures.isEmpty()) {
      return new ComponentMetadataResult(failure(failures));
    }

    String type = json.get(TYPE).getAsString();
    JsonObject wrappedComponent = json.get(COMPONENT).getAsJsonObject();
    ComponentModel model = null;
    if (type.equals(OPERATION)) {
      model = gson.fromJson(wrappedComponent, new TypeToken<OperationModel>() {}.getType());

    } else if (type.equals(SOURCE)) {
      model = gson.fromJson(wrappedComponent, new TypeToken<SourceModel>() {}.getType());
    }

    MetadataAttributes attributes = gson.fromJson(json.get(METADATA_ATTRIBUTES).getAsJsonObject(),
                                                  new TypeToken<MetadataAttributes>() {}.getType());


    return new ComponentMetadataResult(success(ComponentMetadataDescriptor.builder(model).withAttributes(attributes).build()));
  }
}
