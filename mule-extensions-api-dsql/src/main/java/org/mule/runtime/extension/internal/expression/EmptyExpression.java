/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.expression;

import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.QueryTranslator;

/**
 * Represents an empty expression
 *
 * @since 1.0
 */
public class EmptyExpression extends Expression {

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(QueryTranslator queryVisitor) {
    //DO NOTHING ON EMPTY CASE
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() {
    return true;
  }
}
