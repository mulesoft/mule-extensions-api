/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.operator;


import org.mule.runtime.extension.api.dsql.OperatorTranslator;

/**
 * Generic contract for operator elements.
 *
 * @since 1.0
 */
public interface Operator {

  /**
   *
   *
   * @param operatorTranslator an {@link OperatorTranslator} instance.
   * @return
   */
  String accept(OperatorTranslator operatorTranslator);
}
