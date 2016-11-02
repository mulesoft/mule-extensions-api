/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;


/**
 * Default {@link OperatorTranslator} implementation, if no implementation is defined in a {@link QueryTranslator}
 * this one is going to be used.
 *
 * @since 1.0
 */
public class DefaultOperatorTranslator implements OperatorTranslator {

  private static final String LIKE = " like ";
  private static final String GREATER_OR_EQUALS = " >= ";
  private static final String NOT_EQUALS = " <> ";
  private static final String EQUALS = " = ";
  private static final String LESS_OR_EQUALS = " <= ";
  private static final String GREATER = " > ";
  private static final String LESS = " < ";

  /**
   * {@inheritDoc}
   */
  @Override
  public String lessOperator() {
    return LESS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String greaterOperator() {
    return GREATER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String lessOrEqualsOperator() {
    return LESS_OR_EQUALS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String equalsOperator() {
    return EQUALS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String notEqualsOperator() {
    return NOT_EQUALS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String greaterOrEqualsOperator() {
    return GREATER_OR_EQUALS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String likeOperator() {
    return LIKE;
  }
}
