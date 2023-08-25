/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
