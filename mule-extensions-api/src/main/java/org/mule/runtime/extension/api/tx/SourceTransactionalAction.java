/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * The type of beginning actions that sources can take regarding transactions.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public enum SourceTransactionalAction {

  /**
   * Will ensure that a new transaction is created for each invocation
   * <p>
   * J2EE RequiresNew
   */
  ALWAYS_BEGIN,

  /**
   * Whether there is a transaction available or not, ignore it
   * <p>
   * J2EE: NotSupported
   */
  NONE
}
