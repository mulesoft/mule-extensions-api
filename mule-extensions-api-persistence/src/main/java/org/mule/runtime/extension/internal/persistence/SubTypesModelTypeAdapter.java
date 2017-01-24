/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.ObjectTypeReferenceHandler;
import org.mule.runtime.api.meta.model.SubTypesModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link TypeAdapter} implementation which serializes {@link SubTypesModel} instances
 *
 * @since 1.0
 */
public class SubTypesModelTypeAdapter extends TypeAdapter<SubTypesModel> {

  public static final String SUB_TYPES = "subTypes";
  private static final String BASE_TYPE = "baseType";
  private final MetadataTypeGsonTypeAdapter typeAdapter;

  /**
   * Creates a new instance which handles type references through the given
   * {@code referenceHandler}
   *
   * @param referenceHandler an {@link ObjectTypeReferenceHandler}
   */
  public SubTypesModelTypeAdapter(ObjectTypeReferenceHandler referenceHandler) {
    typeAdapter = new MetadataTypeGsonTypeAdapter(referenceHandler);
  }

  @Override
  public void write(JsonWriter out, SubTypesModel value) throws IOException {
    out.beginObject();
    out.name(BASE_TYPE);
    typeAdapter.write(out, value.getBaseType());
    out.name(SUB_TYPES);
    out.beginArray();
    for (MetadataType subType : value.getSubTypes()) {
      typeAdapter.write(out, subType);
    }

    out.endArray();
    out.endObject();
  }

  @Override
  public SubTypesModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    final MetadataType baseType = typeAdapter.fromJsonTree(json.get(BASE_TYPE));

    final Set<MetadataType> subTypes = new LinkedHashSet<>();
    json.get(SUB_TYPES).getAsJsonArray().forEach(subTypeElement -> subTypes.add(typeAdapter.fromJsonTree(subTypeElement)));

    return new SubTypesModel(baseType, subTypes);
  }
}
