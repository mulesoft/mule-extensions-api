/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.runtime.api.tx.TransactionException;

/**
 * Base contract for a component which can participate in a transaction
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.tx.Transactional} instead.
 */
@Deprecated
public interface Transactional {

  /**
   * Begins the transaction
   *
   * @throws TransactionException if the transaction fails to begin
   */
  void begin() throws TransactionException;

  /**
   * Commits the transaction
   *
   * @throws TransactionException if the transaction fails to commit
   */
  void commit() throws TransactionException;

  /**
   * Rolls the transaction back
   *
   * @throws TransactionException if the transaction fails to roll back
   */
  void rollback() throws TransactionException;
}
