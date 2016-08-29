/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
    return this.getClass() == obj.getClass();
  }

}
