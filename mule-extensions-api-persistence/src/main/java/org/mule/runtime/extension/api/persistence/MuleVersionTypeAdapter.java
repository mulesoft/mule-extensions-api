/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.runtime.api.MuleVersion;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * {@link TypeAdapter} implementation which serializes {@link MuleVersion} instances
 *
 * @since 1.0
 */
class MuleVersionTypeAdapter extends TypeAdapter<MuleVersion> {

  @Override
  public void write(JsonWriter out, MuleVersion muleVersion) throws IOException {
    out.value(muleVersion.toCompleteNumericVersion());
  }

  @Override
  public MuleVersion read(JsonReader in) throws IOException {
    return new MuleVersion(in.nextString());
  }
}
