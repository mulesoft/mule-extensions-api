/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * This type adapter maintains backwards compatibility at the serialization levels after the changes done in MULE-19605.
 * <p>
 * TL;DR, {@link NestableElementModel} now has the {@link NestedRouteModel#getMinOccurs()} and
 * {@link NestedRouteModel#getMaxOccurs()} concepts.
 * <p>
 * This adapter allows to still being able to read serialized models which don't contain such properties, by inferring them based
 * on the {@link NestableElementModel#isRequired()} property.
 *
 * @since 1.4.0
 */
public class LegacyNestedElementModelTypeAdapter extends TypeAdapter<NestableElementModel> {

  private static final String MIN_OCCURS_KEY = "minOccurs";
  private static final String MAX_OCCURS_KEY = "maxOccurs";

  private final TypeAdapter<NestableElementModel> delegate;

  public LegacyNestedElementModelTypeAdapter(TypeAdapter<NestableElementModel> delegate) {
    this.delegate = delegate;
  }

  @Override
  public void write(JsonWriter out, NestableElementModel value) throws IOException {
    delegate.write(out, value);
  }

  @Override
  public NestableElementModel read(JsonReader in) throws IOException {
    JsonObject element = new JsonParser().parse(in).getAsJsonObject();
    assureOccurrences(element);

    return delegate.fromJsonTree(element);
  }

  private void assureOccurrences(JsonObject element) {
    final boolean isRequired = isRequired(element);

    JsonElement minOccurs = element.get(MIN_OCCURS_KEY);
    if (minOccurs == null) {
      element.add(MIN_OCCURS_KEY, new JsonPrimitive(isRequired ? 1 : 0));
    }

    JsonElement maxOccurs = element.get(MAX_OCCURS_KEY);
    if (maxOccurs == null) {
      element.add(MAX_OCCURS_KEY, new JsonPrimitive(1));
    }
  }

  private boolean isRequired(JsonObject element) {
    JsonElement isRequired = element.get("isRequired");
    return isRequired != null && isRequired.getAsBoolean();
  }
}
