/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.expression;

import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.Field;
import org.mule.runtime.extension.api.dsql.QueryTranslator;
import org.mule.runtime.extension.api.dsql.Value;
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

  private final Field field;
  private final BinaryOperator operator;
  private final Value value;

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
