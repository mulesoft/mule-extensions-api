/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.declaration.type.TypeUtils;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;

public class TypeUtilsTestCase {

  @Test
  public void getParameterFields() {
    Collection<Field> fields = TypeUtils.getParameterFields(TestObject.class);
    assertThat(fields, hasSize(3));

    assertThat(fields.stream().map(Field::getName).collect(toList()),
               containsInAnyOrder("name", "description", "group"));
  }


  public static class TestObject {

    @Parameter
    private String name;

    @Parameter
    private String description;

    @ParameterGroup(name = "group")
    private TestParameterGroup group;
  }


  public static class TestParameterGroup {

    @Parameter
    private String groupParameter;
  }
}
