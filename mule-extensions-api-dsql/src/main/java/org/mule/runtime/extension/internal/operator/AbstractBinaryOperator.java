/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
