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
 * {@link Value} implementation, that represents a Boolean.
 *
 * @since 1.0
 */
public class BooleanValue extends Value<Boolean> {

  public BooleanValue(Boolean value) {
    super(value);
  }

  public static BooleanValue fromLiteral(String literal) {
    return new BooleanValue(Boolean.parseBoolean(literal));
  }
}
