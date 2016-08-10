/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class HierarchyClassMapTestCase {

  private HierarchyClassMap<Object> map = new HierarchyClassMap<>();
  private Class[] keys = new Class[] {Dog.class, RabidDog.class, Human.class};

  @Before
  public void before() throws Exception {
    for (Class<?> key : keys) {
      map.put(key, key.newInstance());
    }
  }

  @Test
  public void specificGet() {
    Stream.of(keys).forEach(key -> assertThat(map.get(key), is(instanceOf(key))));
  }

  @Test
  public void specificContainsKey() {
    Stream.of(keys).forEach(key -> assertThat(map.containsKey(key), is(true)));
  }

  @Test
  public void hierarchicalGet() {
    assertThat(map.get(RabidDog.class), is(instanceOf(RabidDog.class)));
    map.remove(Dog.class);

    assertThat(map.get(Dog.class), is(instanceOf(RabidDog.class)));
  }

  @Test
  public void hierarchicalContainsKey() {
    assertThat(map.containsKey(RabidDog.class), is(true));
    map.remove(Dog.class);

    assertThat(map.containsKey(Dog.class), is(true));
  }


  public static class Dog {

  }

  public static class RabidDog extends Dog {

  }


  public static class Human {

  }
}
