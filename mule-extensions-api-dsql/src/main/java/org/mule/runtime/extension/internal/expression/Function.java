/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.expression;



import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.QueryTranslator;

/**
 * Represents query expressions which contains functions.
 * <p>
 * E.g.: ... WHERE MAX(field) ...
 *
 * @since 1.0
 */
public class Function extends Expression {

  /**
   * Function expression
   */
  private String function;

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(QueryTranslator queryVisitor) {
    //TODO
  }
}
