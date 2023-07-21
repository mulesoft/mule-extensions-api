/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.extension.internal.persistence.ErrorModelToIdentifierSerializer.deserialize;
import static org.mule.runtime.extension.internal.persistence.ErrorModelToIdentifierSerializer.serialize;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.error.ErrorModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * {@link TypeAdapter} implementation for {@link ErrorModel}, which serializes it as error identifiers. An error identifier
 * follows the {@link ComponentIdentifier} structure, {@code errorNamespace:errorType}.
 *
 * @since 1.0
 */
public class ErrorModelToIdentifierTypeAdapter extends TypeAdapter<ErrorModel> {

  private Map<String, ErrorModel> errorModelRepository = new HashMap<>();

  public ErrorModelToIdentifierTypeAdapter(Map<String, ErrorModel> errorModelMap) {
    errorModelRepository = errorModelMap;
  }

  @Override
  public void write(JsonWriter out, ErrorModel value) throws IOException {
    out.value(serialize(value));
  }

  @Override
  public ErrorModel read(JsonReader in) throws IOException {
    return deserialize(in.nextString(), errorModelRepository);
  }
}
