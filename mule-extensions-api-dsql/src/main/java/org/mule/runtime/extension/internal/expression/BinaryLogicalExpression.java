/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;

/**
 * Base abstract implementation of a binary logical expression.
 *
 * @since 1.0
 */
public abstract class BinaryLogicalExpression extends LogicalExpression {

  protected Expression left;
  protected Expression right;

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

}
