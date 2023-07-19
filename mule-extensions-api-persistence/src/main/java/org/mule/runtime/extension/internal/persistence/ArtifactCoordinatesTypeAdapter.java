/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.artifact.ArtifactCoordinates;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A {@link TypeAdapter} for serializing instances of {@link ArtifactCoordinates}
 *
 * @since 1.5
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
    return new ArtifactCoordinatesImpl(read(json, GROUP_ID), read(json, ARTIFACT_ID), read(json, VERSION));
  }

  private static class ArtifactCoordinatesImpl implements ArtifactCoordinates {

    private final String groupId;
    private final String artifactId;
    private final String version;

    private ArtifactCoordinatesImpl(String groupId, String artifactId, String version) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
    }

    @Override
    public String getGroupId() {
      return groupId;
    }

    @Override
    public String getArtifactId() {
      return artifactId;
    }

    @Override
    public String getVersion() {
      return version;
    }
  }


  private String read(JsonObject json, String memberName) {
    return json.get(memberName).getAsString();
  }
}
