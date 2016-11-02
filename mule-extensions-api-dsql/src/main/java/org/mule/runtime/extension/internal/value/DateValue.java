/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
/**
 *
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents a Date.
 *
 * @since 1.0
 */
public class DateValue extends Value<String> {

  public DateValue(String value) {
    super(value);
  }

  public static DateValue fromLiteral(String literal) {
    return new DateValue(literal);
  }
}
