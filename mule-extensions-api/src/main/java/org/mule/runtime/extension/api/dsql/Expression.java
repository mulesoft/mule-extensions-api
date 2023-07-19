/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
