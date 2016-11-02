/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;


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
