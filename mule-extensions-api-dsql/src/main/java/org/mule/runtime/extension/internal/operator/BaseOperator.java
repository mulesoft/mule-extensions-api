/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
