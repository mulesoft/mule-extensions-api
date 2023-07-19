/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
