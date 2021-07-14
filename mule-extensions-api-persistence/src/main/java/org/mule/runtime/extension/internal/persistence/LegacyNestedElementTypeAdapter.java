/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * This type adapter maintains backwards compatibility at the serialization levels after the changes done in MULE-19579.
 *
 * TL;DR, {@link NestableElementModel} has been promoted to {@link ComponentModel} which transforms the element models into
 * composites. This is a problem for the particular case of {@link NestedRouteModel} since it already was a composite, which
 * children were serialized under the key {@code nestedComponents}. Because of this change, that key would change to
 * {@code childComponents}. This type adapter preserves the old name allowing existing clients to still be able to deserialize old
 * instances.
 *
 * @since 1.4.0
 */
public class LegacyNestedElementTypeAdapter extends TypeAdapter<NestableElementModel> {

  public static final String NESTED_COMPONENTS_KEY = "nestedComponents";
  public static final String CHILD_COMPONENTS_KEY = "childComponents";
  private final TypeAdapter<NestableElementModel> delegate;
  private final Gson gson;

  public LegacyNestedElementTypeAdapter(TypeAdapter<NestableElementModel> delegate, Gson gson) {
    this.delegate = delegate;
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, NestableElementModel value) throws IOException {
    JsonObject element = delegate.toJsonTree(value).getAsJsonObject();
    renameKey(element, NESTED_COMPONENTS_KEY, CHILD_COMPONENTS_KEY);

    gson.toJson(element, out);
  }

  @Override
  public NestableElementModel read(JsonReader in) throws IOException {
    JsonElement element = new JsonParser().parse(in);
    renameKey(element, CHILD_COMPONENTS_KEY, NESTED_COMPONENTS_KEY);

    return delegate.fromJsonTree(element);
  }

  private void renameKey(JsonElement element, String currentKey, String newKey) {
    if (element.isJsonObject()) {
      JsonObject object = element.getAsJsonObject();
      JsonElement target = object.remove(currentKey);
      if (target != null) {
        object.add(newKey, target.deepCopy());
      }
    }
  }
}
