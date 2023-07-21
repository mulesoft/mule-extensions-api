/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * The type of joining actions that operations can take regarding transactions.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
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
