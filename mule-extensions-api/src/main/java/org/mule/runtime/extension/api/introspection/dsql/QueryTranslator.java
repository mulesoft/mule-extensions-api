/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.dsql;

import org.mule.runtime.extension.api.annotation.param.Query;

import java.util.List;

/**
 * This is a translator interface for go over a DSQL {@link Query} structure
 * and easily translate it to a native one.
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
   * Returns an instance of an {@link OperatorTranslator} that is used to translate the
   * the DSQL operators to the Native Query Language operators.
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
