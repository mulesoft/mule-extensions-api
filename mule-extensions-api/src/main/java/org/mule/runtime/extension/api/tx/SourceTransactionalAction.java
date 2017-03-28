/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.tx;

/**
 * The type of beginning actions that sources can take regarding transactions.
 *
 * @since 1.0
 */
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
