/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Represents a value for a query field comparison.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public abstract class Value<T> {

  private T value;

  protected Value(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(getValue());
  }
}
