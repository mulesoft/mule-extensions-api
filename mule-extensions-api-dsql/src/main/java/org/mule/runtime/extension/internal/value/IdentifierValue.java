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
 * {@link Value} implementation, that represents an identifier value.
 *
 * @since 1.0
 */
public class IdentifierValue extends Value<String> {

  IdentifierValue(String value) {
    super(value);
  }

  public static IdentifierValue fromLiteral(String value) {
    if (value.startsWith("[") && value.endsWith("]")) {
      value = value.substring(1, value.length() - 1);
    }
    return new IdentifierValue(value);
  }
}
