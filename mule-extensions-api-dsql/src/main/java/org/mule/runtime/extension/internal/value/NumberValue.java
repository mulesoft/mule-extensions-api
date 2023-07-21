/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents an float value.
 *
 * @since 1.0
 */
public class NumberValue extends Value<Double> {


  public NumberValue(Double value) {
    super(value);
  }


  public static NumberValue fromLiteral(String literal) {
    return new NumberValue(Double.parseDouble(literal));
  }
}
