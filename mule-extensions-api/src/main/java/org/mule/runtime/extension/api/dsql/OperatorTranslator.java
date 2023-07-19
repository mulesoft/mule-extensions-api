/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

/**
 * Provides the possibility to define an operators syntax for translating from the DSQL operators to the specific query language
 * ones.
 *
 * @since 1.0
 */
public interface OperatorTranslator {

  /**
   * Translates the less operator.
   *
   * @return the translated less operator.
   */
  String lessOperator();

  /**
   * Translates the greater operator.
   *
   * @return the translated greater operator.
   */
  String greaterOperator();

  /**
   * Translates the less or equals operator.
   *
   * @return the translated less or equals operator.
   */
  String lessOrEqualsOperator();

  /**
   * Translates the less operator.
   *
   * @return the translated less operator.
   */
  String equalsOperator();

  /**
   * Translates the not equals operator.
   *
   * @return the translated not equals operator.
   */
  String notEqualsOperator();

  /**
   * Translates the greater or equals operator.
   *
   * @return the translated less operator.
   */
  String greaterOrEqualsOperator();

  /**
   * Translates the like operator.
   *
   * @return the translated like operator.
   */
  String likeOperator();
}
