/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.parameter;

import org.mule.runtime.extension.api.runtime.operation.OperationExecutor;

/**
 * Enumerates the different types of support that
 * a given component can provide regarding expressions
 *
 * @since 1.0
 */
public enum ExpressionSupport {
  /**
   * Expressions are required but not enforced. Static values
   * are accepted too.
   */
  SUPPORTED,

  /**
   * Expressions not allowed. Static values only
   */
  NOT_SUPPORTED,

  /**
   * Requires expressions. Static values are not supported
   */
  REQUIRED,

  /**
   * Expressions are allowed but are not going to be evaluated automatically.
   * The value will be supplied into the {@link OperationExecutor} on its literal form.
   */
  LITERAL
}
