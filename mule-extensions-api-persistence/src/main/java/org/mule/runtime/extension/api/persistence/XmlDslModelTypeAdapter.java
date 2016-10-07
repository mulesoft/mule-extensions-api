/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.runtime.extension.api.introspection.XmlDslModel;

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
class XmlDslModelTypeAdapter extends TypeAdapter<XmlDslModel> {

  private static final String NAMESPACE = "namespace";
  private static final String NAMESPACE_URI = "namespaceUri";
  private static final String SCHEMA_LOCATION = "schemaLocation";
  private static final String SCHEMA_VERSION = "schemaVersion";
  private static final String XSD_FILE_NAME = "xsdFileName";

  @Override
  public void write(JsonWriter out, XmlDslModel value) throws IOException {
    out.beginObject();
    out.name(NAMESPACE).value(value.getNamespace());
    out.name(NAMESPACE_URI).value(value.getNamespaceUri());
    out.name(SCHEMA_LOCATION).value(value.getSchemaLocation());
    out.name(SCHEMA_VERSION).value(value.getSchemaVersion());
    out.name(XSD_FILE_NAME).value(value.getXsdFileName());
    out.endObject();
  }

  @Override
  public XmlDslModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return XmlDslModel.builder()
        .setNamespace(read(json, NAMESPACE))
        .setNamespaceUri(read(json, NAMESPACE_URI))
        .setSchemaLocation(read(json, SCHEMA_LOCATION))
        .setSchemaVersion(read(json, SCHEMA_VERSION))
        .setXsdFileName(read(json, XSD_FILE_NAME))
        .build();
  }

  private String read(JsonObject json, String memberName) {
    return json.get(memberName).getAsString();
  }
}
