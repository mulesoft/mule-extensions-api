/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.runtime.api.meta.model.ImportedTypeModel;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * {@link TypeAdapter} implementation which serializes {@link ImportedTypeModel} instances
 *
 * @since 1.0
 */
public class ImportedTypesModelTypeAdapter extends TypeAdapter<ImportedTypeModel> {

  private final MetadataTypeGsonTypeAdapter typeAdapter;

  /**
   * Creates a new instance which doesn't handle type references.
   * Types are described (no ref) in the imported types section.
   *
   */
  public ImportedTypesModelTypeAdapter() {
    typeAdapter = new MetadataTypeGsonTypeAdapter();
  }

  @Override
  public void write(JsonWriter out, ImportedTypeModel value) throws IOException {
    typeAdapter.write(out, value.getImportedType());
  }

  @Override
  public ImportedTypeModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return new ImportedTypeModel((ObjectType) typeAdapter.fromJsonTree(json));
  }
}
