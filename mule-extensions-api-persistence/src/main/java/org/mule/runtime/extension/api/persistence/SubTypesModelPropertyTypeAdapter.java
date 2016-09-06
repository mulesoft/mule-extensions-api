/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.NullObjectTypeReferenceHandler;
import org.mule.metadata.persistence.ObjectTypeReferenceHandler;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * {@link TypeAdapter} implementation which serializes {@link SubTypesModelProperty} instances
 *
 * @since 1.0
 */
class SubTypesModelPropertyTypeAdapter extends TypeAdapter<SubTypesModelProperty> {

  public static final String SUB_TYPES = "subTypes";
  private static final String BASE_TYPE = "baseType";
  private final MetadataTypeGsonTypeAdapter typeAdapter;

  /**
   * Creates a new instance which serializes all types explicitly without
   * handling references
   */
  public SubTypesModelPropertyTypeAdapter() {
    this(new NullObjectTypeReferenceHandler());
  }

  /**
   * Creates a new instance which handles type references through the given
   * {@code referenceHandler}
   *
   * @param referenceHandler an {@link ObjectTypeReferenceHandler}
   */
  public SubTypesModelPropertyTypeAdapter(ObjectTypeReferenceHandler referenceHandler) {
    typeAdapter = new MetadataTypeGsonTypeAdapter(referenceHandler);
  }

  @Override
  public void write(JsonWriter out, SubTypesModelProperty value) throws IOException {
    out.beginArray();
    for (Map.Entry<MetadataType, List<MetadataType>> entry : value.getSubTypesMapping().entrySet()) {
      out.beginObject();
      out.name(BASE_TYPE);
      typeAdapter.write(out, entry.getKey());
      out.name(SUB_TYPES);
      out.beginArray();
      for (MetadataType subType : entry.getValue()) {
        typeAdapter.write(out, subType);
      }

      out.endArray();
      out.endObject();
    }
    out.endArray();
  }

  @Override
  public SubTypesModelProperty read(JsonReader in) throws IOException {
    final Map<MetadataType, List<MetadataType>> subTypesMap = new HashMap<>();
    final JsonArray subTypesArray = new JsonParser().parse(in).getAsJsonArray();

    subTypesArray.iterator().forEachRemaining(importedTypeElement -> {
      final JsonObject tuple = importedTypeElement.getAsJsonObject();
      final MetadataType baseType = typeAdapter.fromJsonTree(tuple.get(BASE_TYPE));
      final List<MetadataType> subTypes = new LinkedList<>();

      tuple.get(SUB_TYPES).getAsJsonArray().forEach(subTypeElement -> subTypes.add(typeAdapter.fromJsonTree(subTypeElement)));
      subTypesMap.put(baseType, subTypes);
    });
    return new SubTypesModelProperty(subTypesMap);
  }
}
