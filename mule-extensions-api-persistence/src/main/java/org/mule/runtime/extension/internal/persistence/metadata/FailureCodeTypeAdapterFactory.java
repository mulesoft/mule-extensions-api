/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import static com.google.gson.stream.JsonToken.NULL;
import org.mule.runtime.api.metadata.resolving.FailureCode;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FailureCodeTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<T> rawType = (Class<T>) type.getRawType();
    if (rawType.equals(FailureCode.class))
      return (TypeAdapter<T>) new TypeAdapter<FailureCode>() {

        @Override
        public void write(JsonWriter out, FailureCode value) throws IOException {
          if (value == null) {
            out.nullValue();
            return;
          }
          out.value(value.getName());
        }

        @Override
        public FailureCode read(JsonReader in) throws IOException {
          if (in.peek() == NULL) {
            in.nextNull();
            return null;
          }

          return new FailureCode(in.nextString());
        }
      };
    return null;
  }

}
