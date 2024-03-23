/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import static java.util.Collections.unmodifiableList;
import static org.mule.runtime.internal.util.collection.UnmodifiableMap.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueDeclarer {

  public static SimpleValue ofSimpleValue(Object value) {
    return new ImmutableSimpleValue(value);
  }

  public static ArrayValueBuilder ofArrayValue() {
    return new ArrayValueBuilder();
  }

  public static MapValueBuilder ofMapValue() {
    return new MapValueBuilder();
  }

  public static ObjectValueBuilder ofObjectValue() {
    return new ObjectValueBuilder();
  }

  public static final class ArrayValueBuilder {

    private List<Value> arrayValues = new ArrayList<>();

    public ArrayValueBuilder withItemValue(Value value) {
      arrayValues.add(value);
      return this;
    }

    public ArrayValueBuilder withItemValues(List<Value> values) {
      arrayValues.addAll(values);
      return this;
    }

    public ArrayValue build() {
      return new ImmutableArrayValue(arrayValues);
    }
  }

  public static final class MapValueBuilder {

    private Map<String, Value> mapValues = new HashMap<>();

    public MapValueBuilder withMapEntry(String entryName, Value entryValue) {
      mapValues.put(entryName, entryValue);
      return this;
    }

    public MapValueBuilder withMapEntries(Map<String, Value> mapEntries) {
      mapValues.putAll(mapEntries);
      return this;
    }

    public MapValue build() {
      return new ImmutableMapValue(mapValues);
    }
  }

  public static final class ObjectValueBuilder {

    private Map<String, Value> objectFieldValues = new HashMap<>();

    public ObjectValueBuilder withFieldValue(String fieldName, Value fieldValue) {
      objectFieldValues.put(fieldName, fieldValue);
      return this;
    }

    public ObjectValueBuilder withFieldValues(Map<String, Value> fieldValues) {
      objectFieldValues.putAll(fieldValues);
      return this;
    }

    public ImmutableObjectValue build() {
      return new ImmutableObjectValue(objectFieldValues);
    }
  }

  private static final class ImmutableArrayValue implements ArrayValue {

    private final List<Value> values;

    public ImmutableArrayValue(List<Value> values) {
      this.values = unmodifiableList(values);
    }

    @Override
    public List<Value> getValues() {
      return values;
    }

  }

  private static final class ImmutableMapValue implements MapValue {

    private final Map<String, Value> mapValues;

    public ImmutableMapValue(Map<String, Value> mapValues) {
      this.mapValues = unmodifiableMap(mapValues);
    }

    @Override
    public Map<String, Value> getMapValues() {
      return mapValues;
    }
  }

  private static final class ImmutableObjectValue implements ObjectValue {

    private final Map<String, Value> fieldValues;

    public ImmutableObjectValue(Map<String, Value> fieldValues) {
      this.fieldValues = unmodifiableMap(fieldValues);
    }

    @Override
    public Map<String, Value> getObjectFieldValues() {
      return fieldValues;
    }
  }

  private static final class ImmutableSimpleValue implements SimpleValue {

    private final Object value;

    public ImmutableSimpleValue(Object value) {
      this.value = value;
    }

    @Override
    public Object getValue() {
      return value;
    }
  }

}
