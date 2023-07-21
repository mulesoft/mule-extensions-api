/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents an integer.
 *
 * @since 1.0
 */
public class IntegerValue extends Value<Integer> {

  public IntegerValue(Integer value) {
    super(value);
  }

  public static IntegerValue fromLiteral(String literal) {
    return new IntegerValue(Integer.parseInt(literal));
  }
}
