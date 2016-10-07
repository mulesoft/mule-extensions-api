/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.ObjectTypeReferenceHandler;
import org.mule.runtime.extension.api.introspection.ImportedTypeModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * {@link TypeAdapter} implementation which serializes {@link ImportedTypeModel} instances
 *
 * @since 1.0
 */
class ImportedTypesModelTypeAdapter extends TypeAdapter<ImportedTypeModel> {

  private static final String EXTENSION = "extension";
  private static final String TYPE = "type";
  private final MetadataTypeGsonTypeAdapter typeAdapter;

  /**
   * Creates a new instance which handles type references through the given
   * {@code referenceHandler}
   *
   * @param referenceHandler an {@link ObjectTypeReferenceHandler}
   */
  public ImportedTypesModelTypeAdapter(ObjectTypeReferenceHandler referenceHandler) {
    typeAdapter = new MetadataTypeGsonTypeAdapter(referenceHandler);
  }

  @Override
  public void write(JsonWriter out, ImportedTypeModel value) throws IOException {
    out.beginObject();
    out.name(TYPE);
    typeAdapter.write(out, value.getImportedType());
    out.name(EXTENSION).value(value.getOriginExtensionName());
    out.endObject();
  }

  @Override
  public ImportedTypeModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return new ImportedTypeModel(json.get(EXTENSION).getAsString(), typeAdapter.fromJsonTree(json.get(TYPE)));
  }
}
