/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 *
 */
package org.mule.runtime.extension.internal.value;

import org.mule.runtime.extension.api.dsql.Value;

/**
 * {@link Value} implementation, that represents a MEL.
 *
 * @since 1.0
 */
public class MuleExpressionValue extends Value<String> {

  protected MuleExpressionValue(String value) {
    super(value);
  }

  public static MuleExpressionValue fromLiteral(String value) {
    return new MuleExpressionValue(value);
  }
}
