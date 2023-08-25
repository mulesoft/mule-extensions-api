/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.expression;


import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.QueryTranslator;

/**
 * Represents the NOT expression
 *
 * @since 1.0
 */
public class Not extends UnaryLogicalExpression {

  public Not(Expression expression) {
    this.expression = expression;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(QueryTranslator queryVisitor) {
    //TODO
  }
}
