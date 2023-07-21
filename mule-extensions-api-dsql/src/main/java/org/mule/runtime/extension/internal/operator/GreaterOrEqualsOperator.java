/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.operator;

import org.mule.runtime.extension.api.dsql.OperatorTranslator;

/**
 * Represents a less or equals operator
 */
public class GreaterOrEqualsOperator extends AbstractBinaryOperator {

  @Override
  public String accept(OperatorTranslator operatorTranslator) {
    return operatorTranslator.greaterOrEqualsOperator();
  }
}
