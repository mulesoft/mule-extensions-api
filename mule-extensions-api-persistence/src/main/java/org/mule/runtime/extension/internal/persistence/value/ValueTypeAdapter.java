/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.value;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ValueTypeAdapter extends TypeAdapter<Value> {

  private static final String ID = "id";
  private static final String DISPLAY_NAME = "displayName";
  private static final String PART_NAME = "partName";
  private static final String CHILDS = "childs";

  @Override
  public void write(JsonWriter jsonWriter, Value value) throws IOException {
    if (value != null) {
      jsonWriter.beginObject();
      jsonWriter.name(ID).value(value.getId());
      jsonWriter.name(DISPLAY_NAME).value(value.getDisplayName());
      jsonWriter.name(PART_NAME).value(value.getPartName());

      jsonWriter.name(CHILDS);
      jsonWriter.beginArray();
      for (Value child : value.getChilds()) {
        write(jsonWriter, child);
      }
      jsonWriter.endArray();
      value.getChilds();

      jsonWriter.endObject();
    }
  }

  @Override
  public Value read(JsonReader jsonReader) throws IOException {
    return doRead(jsonReader).build();
  }

  private ValueBuilder doRead(JsonReader jsonReader) throws IOException {
    if (jsonReader != null) {
      String id = null;
      String displayName = null;
      String partName = null;
      Set<ValueBuilder> childs = new LinkedHashSet<>();

      jsonReader.beginObject();
      while (jsonReader.hasNext()) {
        switch (jsonReader.nextName()) {
          case ID:
            id = jsonReader.nextString();
            break;
          case DISPLAY_NAME:
            displayName = jsonReader.nextString();
            break;
          case PART_NAME:
            partName = jsonReader.nextString();
            break;
          case CHILDS:
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
              childs.add(doRead(jsonReader));
            }
            jsonReader.endArray();
            break;
        }
      }
      jsonReader.endObject();
      ValueBuilder valueBuilder = ValueBuilder.newValue(id, partName).withDisplayName(displayName);
      childs.forEach(childValueBuilder -> valueBuilder.withChild(childValueBuilder));
      return valueBuilder;
    }

    return null;
  }

}
