/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.MuleVersion;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * {@link TypeAdapter} implementation which serializes {@link MuleVersion} instances
 *
 * @since 1.0
 */
public class MuleVersionTypeAdapter extends TypeAdapter<MuleVersion> {

  @Override
  public void write(JsonWriter out, MuleVersion muleVersion) throws IOException {
    out.value(muleVersion.toCompleteNumericVersion());
  }

  @Override
  public MuleVersion read(JsonReader in) throws IOException {
    return new MuleVersion(in.nextString());
  }
}
