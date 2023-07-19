/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

import org.mule.runtime.extension.api.annotation.param.Query;

import java.util.List;

/**
 * This is a translator interface for go over a DSQL {@link Query} structure and easily translate it to a native one.
 *
 * @since 1.0
 */
public interface QueryTranslator {

  void translateFields(List<Field> fields);

  void translateTypes(EntityType types);

  void translateOrderByFields(List<Field> orderByFields, Direction direction);

  void translateAnd();

  void translateOR();

  void translateComparison(String operator, Field field, Value<?> value);

  void translateBeginExpression();

  void translateInitPrecedence();

  void translateEndPrecedence();

  void translateLimit(int limit);

  void translateOffset(int offset);

  /**
   * Returns an instance of an {@link OperatorTranslator} that is used to translate the the DSQL operators to the Native Query
   * Language operators.
   */
  default OperatorTranslator operatorTranslator() {
    return new DefaultOperatorTranslator();
  }

  /**
   * Returns the final translated query.
   *
   * @return an {@link String} with the query in Native Query Language.
   */
  String getTranslation();
}
