/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
