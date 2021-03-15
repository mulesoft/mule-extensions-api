/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.runtime.extension.api.declaration.type.annotation.DefaultImplementingTypeAnnotation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * {@link TypeAdapter} implementation which serializes {@link DefaultImplementingTypeAnnotation} instances
 *
 * @since 1.0
 */
public class DefaultImplementingTypeAnnotationTypeAdapter extends TypeAdapter<DefaultImplementingTypeAnnotation> {

  private final MetadataTypeGsonTypeAdapter typeAdapter;

  /**
   * Creates a new instance which doesn't handle type references. Types are described (no ref) in the imported types section.
   */
  public DefaultImplementingTypeAnnotationTypeAdapter() {
    typeAdapter = new MetadataTypeGsonTypeAdapter();
  }

  @Override
  public void write(JsonWriter out, DefaultImplementingTypeAnnotation value) throws IOException {
    typeAdapter.write(out, value.getDefaultType());
  }

  @Override
  public DefaultImplementingTypeAnnotation read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return new DefaultImplementingTypeAnnotation(typeAdapter.fromJsonTree(json));
  }
}
