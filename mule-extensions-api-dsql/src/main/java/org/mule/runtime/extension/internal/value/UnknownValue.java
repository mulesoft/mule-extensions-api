/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 *
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents an unknown value.
 *
 * @since 1.0
 */
public class UnknownValue extends Value<String> {

  private UnknownValue(String value) {
    super(value);
  }

  public static UnknownValue fromLiteral(String value) {
    return new UnknownValue(value);
  }
}
