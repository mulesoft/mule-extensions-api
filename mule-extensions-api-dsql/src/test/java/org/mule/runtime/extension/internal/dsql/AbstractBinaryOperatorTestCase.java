/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.dsql;

import static org.junit.Assert.assertEquals;
import org.mule.runtime.extension.internal.operator.EqualsOperator;
import org.mule.runtime.extension.internal.operator.GreaterOperator;
import org.mule.runtime.extension.internal.operator.GreaterOrEqualsOperator;
import org.mule.runtime.extension.internal.operator.LessOperator;
import org.mule.runtime.extension.internal.operator.LessOrEqualsOperator;
import org.mule.runtime.extension.internal.operator.LikeOperator;
import org.mule.runtime.extension.internal.operator.NotEqualsOperator;

import org.junit.Test;


public class AbstractBinaryOperatorTestCase {

  @Test
  public void testOperationsSymbols() {
    assertEquals(" < ", (new LessOperator()).toString());
    assertEquals(" <= ", (new LessOrEqualsOperator()).toString());
    assertEquals(" = ", (new EqualsOperator()).toString());
    assertEquals(" > ", (new GreaterOperator()).toString());
    assertEquals(" >= ", (new GreaterOrEqualsOperator()).toString());
    assertEquals(" <> ", (new NotEqualsOperator()).toString());
    assertEquals(" like ", (new LikeOperator()).toString());
  }
}
