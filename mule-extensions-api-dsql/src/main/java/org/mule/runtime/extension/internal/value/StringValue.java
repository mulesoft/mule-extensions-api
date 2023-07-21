/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents a String.
 *
 * @since 1.0
 */
public class StringValue extends Value<String> {

  public StringValue(String value) {
    super(value);
  }

  public static StringValue fromLiteral(String literal) {
    String value = literal;
    if (literal.startsWith("\'") && literal.endsWith("\'")) {
      value = value.substring(1, literal.length() - 1);
    }
    return new StringValue(value);
  }

  public String getValueWrappedWith(char wrap) {
    return wrap + getValue() + wrap;
  }

  @Override
  public String toString() {
    return "'" + getValue() + "'";
  }


}
