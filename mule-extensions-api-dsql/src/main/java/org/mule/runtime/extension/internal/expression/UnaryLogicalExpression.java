/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;

import org.mule.runtime.extension.api.dsql.Expression;

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
