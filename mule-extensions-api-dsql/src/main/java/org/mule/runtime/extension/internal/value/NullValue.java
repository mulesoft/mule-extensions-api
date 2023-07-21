/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 *
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents a null value.
 *
 * @since 1.0
 */
public class NullValue extends Value<Object> {

  public NullValue() {
    super(null);
  }
}
