/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.tx;

/**
 * The type of joining actions that operations can take regarding transactions.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.tx.OperationTransactionalAction} instead.
 */
@Deprecated
public enum OperationTransactionalAction {
  /**
   * There must always be a transaction present for the invocation
   * <p>
   * J2EE: Mandatory
   */
  ALWAYS_JOIN,

  /**
   * If there is a transaction available, then use it, otherwise continue processing
   * <p>
   * J2EE: Supports
   */
  JOIN_IF_POSSIBLE,


  /**
   * Executes outside any existent transaction
   */
  NOT_SUPPORTED
}
