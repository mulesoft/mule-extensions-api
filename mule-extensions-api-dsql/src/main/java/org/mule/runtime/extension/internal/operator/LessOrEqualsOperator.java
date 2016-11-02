/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.operator;

import org.mule.runtime.extension.api.dsql.OperatorTranslator;

/**
 * Represents an less or equals operator
 */
public class LessOrEqualsOperator extends AbstractBinaryOperator {

  @Override
  public String accept(OperatorTranslator operatorTranslator) {
    return operatorTranslator.lessOrEqualsOperator();
  }
}
