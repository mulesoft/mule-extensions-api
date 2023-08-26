/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.operator;

/**
 * Base abstract implementation of an {@link Operator}.
 *
 * @since 1.0
 */
abstract class BaseOperator implements Operator {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    return obj != null && this.getClass() == obj.getClass();
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }
}
