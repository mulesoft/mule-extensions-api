/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents a Date Time value.
 *
 * @since 1.0
 */
public class DateTimeValue extends Value<String> {

  public DateTimeValue(String value) {
    super(value);
  }

  public static DateTimeValue fromLiteral(String literal) {
    return new DateTimeValue(literal);
  }
}
