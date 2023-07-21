/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
