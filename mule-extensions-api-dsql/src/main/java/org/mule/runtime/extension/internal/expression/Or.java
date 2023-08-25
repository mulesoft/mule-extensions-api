/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.expression;


import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.QueryTranslator;

/**
 * Represents the logical expression OR
 *
 * @since 1.0
 */
public class Or extends BinaryLogicalExpression {

  public Or(Expression left, Expression right) {
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
    queryVisitor.translateOR();
    right.accept(queryVisitor);
    queryVisitor.translateEndPrecedence();
  }
}
