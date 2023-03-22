/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

/**
 * Generic contract for a DSQL expression.
 *
 * @since 1.0
 */
public abstract class Expression {

  /**
   * Translates an expression using the specified {@code queryTranslator} from DSQL into an expression in Native Query Language.
   *
   * @param queryTranslator a {@link QueryTranslator} instance.
   */
  public abstract void accept(QueryTranslator queryTranslator);

  /**
   * @return whether the expression is empty or not.
   */
  public boolean isEmpty() {
    return false;
  }
}
