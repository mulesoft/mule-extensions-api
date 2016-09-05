/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;

import org.mule.runtime.extension.api.introspection.dsql.QueryTranslator;

/**
 * Expression that represents the AND logical condition
 *
 * @since 1.0
 */
public class And extends BinaryLogicalExpression {

  public And(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(QueryTranslator queryVisitor) {
    queryVisitor.translateInitPrecedence();
    left.accept(queryVisitor);
    queryVisitor.translateAnd();
    right.accept(queryVisitor);
    queryVisitor.translateEndPrecedence();
  }
}
