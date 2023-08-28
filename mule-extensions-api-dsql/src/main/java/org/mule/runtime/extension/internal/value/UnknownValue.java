/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
