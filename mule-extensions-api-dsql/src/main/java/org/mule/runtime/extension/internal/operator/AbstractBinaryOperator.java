/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.operator;


import org.mule.runtime.extension.api.dsql.DefaultOperatorTranslator;

/**
 * Base contract for all binary operators.
 *
 * @since 1.0
 */
abstract class AbstractBinaryOperator extends BaseOperator implements BinaryOperator {

  @Override
  public String toString() {
    return accept(new DefaultOperatorTranslator());
  }
}
