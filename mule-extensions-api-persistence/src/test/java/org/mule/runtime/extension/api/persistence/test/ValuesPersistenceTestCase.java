/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.test;

import static org.mule.runtime.api.value.ValueResult.resultFrom;
import static org.mule.runtime.extension.api.values.ValueBuilder.newValue;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.api.meta.model.parameter.ActingParameterModel;
import org.mule.runtime.api.meta.model.parameter.ValueProviderModel;
import org.mule.runtime.api.value.ResolvingFailure;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.api.value.ValueResult;
import org.mule.runtime.extension.api.model.parameter.ImmutableActingParameterModel;
import org.mule.runtime.extension.api.persistence.value.ValueResultJsonSerializer;
import org.mule.runtime.extension.api.values.ImmutableValue;
import org.mule.runtime.extension.internal.persistence.DefaultImplementationTypeAdapterFactory;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import org.junit.Test;

public class ValuesPersistenceTestCase {

  private static final ValueResult FAILURE_VALUE_RESULT =
      resultFrom(ResolvingFailure.Builder.newFailure().withFailureCode("FAILURE_CODE").build());
  private static final Value MULTI_LEVEL_VALUE =
      newValue("root").withChild(newValue("level1").withChild(newValue("level2"))).build();
  private static final ValueProviderModel VALUE_PROVIDER_MODEL =
      new ValueProviderModel(asList(buildActingParameterModel("param1", true), buildActingParameterModel("param2", true),
                                    buildActingParameterModel("param3", false)),
                             false, false, false, 1,
                             "Category 1", "Id 1");
  private static final ValueResult MULTI_LEVEL_VALUE_RESULT = resultFrom(singleton(MULTI_LEVEL_VALUE));

  private final JsonParser jsonParser = new JsonParser();
  private final ValueResultJsonSerializer valueResultJsonSerializer = new ValueResultJsonSerializer();
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(Value.class, ImmutableValue.class))
      .registerTypeAdapterFactory(new DefaultImplementationTypeAdapterFactory<>(ActingParameterModel.class,
                                                                                ImmutableActingParameterModel.class))
      .create();

  @Test
  public void serializePartModelProperty() throws IOException {
    JsonElement serialized = gson.toJsonTree(VALUE_PROVIDER_MODEL);
    JsonElement expected = loadAsJson("/values/values-provider-model.json");
    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializedPartModelProperty() throws IOException {
    ValueProviderModel valueProviderModel =
        gson.fromJson(loadAsJson("/values/values-provider-model.json"), ValueProviderModel.class);
    assertThat(valueProviderModel, is(VALUE_PROVIDER_MODEL));
  }

  @Test
  public void serializeValuesResult() throws IOException {
    JsonElement serialized = gson.toJsonTree(MULTI_LEVEL_VALUE);
    JsonElement expected = loadAsJson("/values/values.json");
    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializeValuesResult() throws IOException {
    Value values = gson.fromJson(loadAsJson("/values/values.json"), new TypeToken<Value>() {}.getType());
    assertThat(MULTI_LEVEL_VALUE, is(values));
  }

  @Test
  public void deserializeMultiLevelValueResult() throws IOException {
    ValueResult deserialize = valueResultJsonSerializer.deserialize(loadAsString("/values/multi-level-value-result.json"));
    assertThat(deserialize, is(MULTI_LEVEL_VALUE_RESULT));
  }

  @Test
  public void serializeMultiLevelValueResult() throws IOException {
    String serialize = valueResultJsonSerializer.serialize(MULTI_LEVEL_VALUE_RESULT);
    assertThat(jsonParser.parse(serialize), is(loadAsJson("/values/multi-level-value-result.json")));
  }

  @Test
  public void serializeFailureValueResult() throws IOException {
    String serialize = valueResultJsonSerializer.serialize(FAILURE_VALUE_RESULT);
    assertThat(jsonParser.parse(serialize), is(loadAsJson("/values/failure-value-result.json")));
  }

  @Test
  public void deserializeFailureValueResult() throws IOException {
    ValueResult deserialize = valueResultJsonSerializer.deserialize(loadAsString("/values/failure-value-result.json"));
    assertThat(deserialize, is(FAILURE_VALUE_RESULT));
  }

  private JsonElement loadAsJson(String name) throws IOException {
    return jsonParser.parse(loadAsString(name));
  }

  private String loadAsString(String name) throws IOException {
    return IOUtils.toString(this.getClass().getResourceAsStream(name));
  }

  private static ActingParameterModel buildActingParameterModel(String name, boolean required) {
    return new ImmutableActingParameterModel(name, required);
  }
}
