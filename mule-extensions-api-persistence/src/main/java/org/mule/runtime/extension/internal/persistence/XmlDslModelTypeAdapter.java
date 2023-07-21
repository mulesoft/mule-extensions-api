/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.XmlDslModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A {@link TypeAdapter} for serializing instances of {@link XmlDslModel}
 *
 * @since 1.0
 */
public class XmlDslModelTypeAdapter extends TypeAdapter<XmlDslModel> {

  private static final String PREFIX = "prefix";
  private static final String NAMESPACE = "namespace";
  private static final String SCHEMA_LOCATION = "schemaLocation";
  private static final String SCHEMA_VERSION = "schemaVersion";
  private static final String XSD_FILE_NAME = "xsdFileName";

  @Override
  public void write(JsonWriter out, XmlDslModel value) throws IOException {
    out.beginObject();
    out.name(PREFIX).value(value.getPrefix());
    out.name(NAMESPACE).value(value.getNamespace());
    out.name(SCHEMA_LOCATION).value(value.getSchemaLocation());
    out.name(SCHEMA_VERSION).value(value.getSchemaVersion());
    out.name(XSD_FILE_NAME).value(value.getXsdFileName());
    out.endObject();
  }

  @Override
  public XmlDslModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return XmlDslModel.builder()
        .setPrefix(read(json, PREFIX))
        .setNamespace(read(json, NAMESPACE))
        .setSchemaLocation(read(json, SCHEMA_LOCATION))
        .setSchemaVersion(read(json, SCHEMA_VERSION))
        .setXsdFileName(read(json, XSD_FILE_NAME))
        .build();
  }

  private String read(JsonObject json, String memberName) {
    return json.get(memberName).getAsString();
  }
}
