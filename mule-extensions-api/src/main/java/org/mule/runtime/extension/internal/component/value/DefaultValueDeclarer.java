/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.component.value;

import org.mule.runtime.extension.api.component.value.ArrayValueDeclarer;
import org.mule.runtime.extension.api.component.value.MapValueDeclarer;
import org.mule.runtime.extension.api.component.value.ObjectValueDeclarer;
import org.mule.runtime.extension.api.component.value.ValueDeclarer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultValueDeclarer implements ValueDeclarer {

  private HasValue value;

  @Override
  public MapValueDeclarer asMapValue() {
    value = new DefaultMapValueDeclarer();
    return (MapValueDeclarer) value;
  }

  @Override
  public ObjectValueDeclarer asObjectValue() {
    value = new DefaultObjectValueDeclarer();
    return (DefaultObjectValueDeclarer) value;
  }

  @Override
  public ArrayValueDeclarer asArrayValue() {
    value = new DefaultArrayValueDeclarer();
    return (ArrayValueDeclarer) value;
  }

  @Override
  public void withValue(Object value) {
    this.value = new SimpleHasValue(value);
  }

  public Object getValue() {
    return value.getValue();
  }

  private static Object getValue(Consumer<ValueDeclarer> valueDeclarerConsumer) {
    DefaultValueDeclarer valueDeclarer = new DefaultValueDeclarer();
    valueDeclarerConsumer.accept(valueDeclarer);
    return valueDeclarer.getValue();
  }

  private interface HasValue {

    Object getValue();
  }

  private static class DefaultMapValueDeclarer implements MapValueDeclarer, HasValue {

    private final Map<String, Object> mapValue;

    public DefaultMapValueDeclarer() {
      mapValue = new HashMap();
    }

    @Override
    public MapValueDeclarer withEntry(String name, Object value) {
      mapValue.put(name, value);
      return this;
    }

    @Override
    public MapValueDeclarer withEntry(String name, Consumer<ValueDeclarer> valueDeclarerConsumer) {
      mapValue.put(name, DefaultValueDeclarer.getValue(valueDeclarerConsumer));
      return this;
    }


    @Override
    public Object getValue() {
      return mapValue;
    }
  }

  private static class DefaultObjectValueDeclarer implements ObjectValueDeclarer, HasValue {

    private final Map<String, Object> mapValue;

    public DefaultObjectValueDeclarer() {
      mapValue = new HashMap();
    }

    @Override
    public ObjectValueDeclarer withField(String name, Object value) {
      mapValue.put(name, value);
      return this;
    }

    @Override
    public ObjectValueDeclarer withField(String name, Consumer<ValueDeclarer> valueDeclarerConsumer) {
      mapValue.put(name, DefaultValueDeclarer.getValue(valueDeclarerConsumer));
      return this;
    }

    @Override
    public Object getValue() {
      return mapValue;
    }
  }

  private static class DefaultArrayValueDeclarer implements ArrayValueDeclarer, HasValue {

    private final List<Object> listValue;

    public DefaultArrayValueDeclarer() {
      listValue = new ArrayList<>();
    }

    @Override
    public ArrayValueDeclarer withItem(Object value) {
      listValue.add(value);
      return this;
    }

    @Override
    public ArrayValueDeclarer withItem(Consumer<ValueDeclarer> valueDeclarerConsumer) {
      listValue.add(DefaultValueDeclarer.getValue(valueDeclarerConsumer));
      return this;
    }

    @Override
    public Object getValue() {
      return listValue;
    }
  }

  private static class SimpleHasValue implements HasValue {

    private final Object value;

    public SimpleHasValue(Object value) {
      this.value = value;
    }

    @Override
    public Object getValue() {
      return value;
    }
  }

}
