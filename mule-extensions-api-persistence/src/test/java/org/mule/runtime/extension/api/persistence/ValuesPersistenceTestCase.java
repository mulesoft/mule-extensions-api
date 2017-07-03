/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.mule.runtime.api.meta.model.parameter.ValuesProviderModel;
import org.mule.runtime.api.values.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class ValuesPersistenceTestCase {

  private final Value value =
      ValueBuilder.newValue("root").withChild(ValueBuilder.newValue("level1").withChild(ValueBuilder.newValue("level2"))).build();
  private ValuesProviderModel valuesProviderModel =
      new ValuesProviderModel(Arrays.asList("param1", "param2"), 1, "Category 1");
  private Gson gson = new Gson();

  @Test
  public void serializePartModelProperty() throws IOException {
    JsonElement serialized = gson.toJsonTree(valuesProviderModel);
    JsonElement expected = load("values/values-provider-model.json");
    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializedPartModelProperty() throws IOException {
    ValuesProviderModel valuesProviderModel = gson.fromJson(load("values/values-provider-model.json"), ValuesProviderModel.class);
    assertThat(valuesProviderModel, is(this.valuesProviderModel));
  }

  @Test
  public void serializeValuesResult() throws IOException {
    JsonElement serialized = gson.toJsonTree(value);
    JsonElement expected = load("values/values.json");
    assertThat(serialized, is(expected));
  }

  @Test
  @Ignore("MULE-12857: Implement Parameters Options Resolver in Mule's Side")
  public void deserializeValuesResult() throws IOException {
    Value values = gson.fromJson(load("values/values.json"), new TypeToken<Value>() {}.getType());
    assertThat(value, is(values));
  }

  private JsonElement load(String name) throws IOException {
    return new JsonParser().parse(IOUtils
        .toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));
  }
}
