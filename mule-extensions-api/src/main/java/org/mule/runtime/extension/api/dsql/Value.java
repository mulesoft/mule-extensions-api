/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Represents a value for a query field comparison.
 *
 * @since 1.0
 */
@MinMuleVersion("4.0")
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
