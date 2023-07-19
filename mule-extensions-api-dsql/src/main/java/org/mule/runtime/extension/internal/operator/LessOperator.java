/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.operator;

import org.mule.runtime.extension.api.dsql.OperatorTranslator;

/**
 * Represent a less operator
 */
public class LessOperator extends AbstractBinaryOperator {

  @Override
  public String accept(OperatorTranslator operatorTranslator) {
    return operatorTranslator.lessOperator();
  }
}
