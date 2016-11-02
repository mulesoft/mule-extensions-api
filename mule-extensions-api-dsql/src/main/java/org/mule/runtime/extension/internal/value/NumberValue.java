/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
