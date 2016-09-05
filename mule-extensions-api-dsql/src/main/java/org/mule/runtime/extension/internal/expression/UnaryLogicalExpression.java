/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;

/**
 * Base class for unary expressions.
 *
 * @since 1.0
 */
abstract class UnaryLogicalExpression extends LogicalExpression {

  protected Expression expression;

  public Expression getRight() {
    return expression;
  }
}
