/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.expression;

import org.mule.runtime.extension.api.introspection.dsql.Field;
import org.mule.runtime.extension.api.introspection.dsql.QueryTranslator;
import org.mule.runtime.extension.api.introspection.dsql.Value;
import org.mule.runtime.extension.internal.operator.BinaryOperator;

/**
 * Represents a comparison expression.
 *
 * @since 1.0
 */
public class FieldComparison extends Expression {

  public FieldComparison(BinaryOperator operator, Field field, Value value) {
    this.operator = operator;
    this.field = field;
    this.value = value;
  }

  private Field field;
  private BinaryOperator operator;
  private Value value;

  public Field getField() {
    return field;
  }

  public BinaryOperator getOperator() {
    return operator;
  }

  public Value getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(QueryTranslator queryVisitor) {
    queryVisitor.translateComparison(operator.accept(queryVisitor.operatorTranslator()), this.field, this.value);
  }
}
