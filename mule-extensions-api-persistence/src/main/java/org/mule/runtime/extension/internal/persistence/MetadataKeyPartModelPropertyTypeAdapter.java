/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.extension.api.property.MetadataKeyPartModelProperty;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Allows for maintaining backward compatibility when deserializing {@link MetadataKeyPartModelProperty}.
 *
 * @since 1.8.0
 */
public class MetadataKeyPartModelPropertyTypeAdapter extends TypeAdapter<MetadataKeyPartModelProperty> {

  private final TypeAdapter<MetadataKeyPartModelProperty> delegate;

  public MetadataKeyPartModelPropertyTypeAdapter(TypeAdapter<MetadataKeyPartModelProperty> delegate) {
    this.delegate = delegate;
  }

  @Override
  public void write(JsonWriter out, MetadataKeyPartModelProperty value) throws IOException {
    // Delegates to the default implementation
    delegate.write(out, value);
  }

  @Override
  public MetadataKeyPartModelProperty read(JsonReader in) throws IOException {
    MetadataKeyPartModelProperty result = delegate.read(in);

    // This field may not be present, in that case we delegate to the constructor to assign it a default value
    if (result.getExpressionSupport() == null) {
      result = new MetadataKeyPartModelProperty(result.getOrder(), result.isProvidedByKeyResolver());
    }

    return result;
  }
}
