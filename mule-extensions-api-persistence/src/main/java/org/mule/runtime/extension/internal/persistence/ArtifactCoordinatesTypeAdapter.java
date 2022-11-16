/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.artifact.ArtifactCoordinates;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.module.artifact.api.descriptor.BundleDescriptor.Builder;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A {@link TypeAdapter} for serializing instances of {@link ParameterDslConfiguration}
 *
 * @since 1.0
 */
public class ArtifactCoordinatesTypeAdapter extends TypeAdapter<ArtifactCoordinates> {

  private static final String ARTIFACT_ID = "artifact id";
  private static final String GROUP_ID = "group id";
  private static final String VERSION = "version";

  @Override
  public void write(JsonWriter out, ArtifactCoordinates value) throws IOException {
    if (value != null) {
      out.beginObject();
      out.name(ARTIFACT_ID).value(value.getArtifactId());
      out.name(GROUP_ID).value(value.getGroupId());
      out.name(VERSION).value(value.getVersion());
      out.endObject();
    }
  }

  @Override
  public ArtifactCoordinates read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    return new Builder()
        .setArtifactId(read(json, ARTIFACT_ID))
        .setGroupId(read(json, GROUP_ID))
        .setVersion(read(json, VERSION))
        .build();
  }

  private String read(JsonObject json, String memberName) {
    return json.get(memberName).getAsString();
  }
}
