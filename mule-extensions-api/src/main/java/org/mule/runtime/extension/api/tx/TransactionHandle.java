/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.tx.TransactionException;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;

/**
 * Allows to manipulate the transaction that was started and bound to an specific {@link SourceCallbackContext}
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.tx.TransactionHandle} instead.
 */
@NoImplement
@Deprecated
public interface TransactionHandle {

  /**
   * @return Whether a transaction has been attached to the owning {@link SourceCallbackContext}
   */
  boolean isTransacted();

  /**
   * Commits the transaction. This method is idempotent and thread-safe. It will do nothing if this method
   * or {@link #rollback()} have already been invoked on {@code this} instance.
   *
   * @throws TransactionException if the transaction fails to commit
   */
  void commit() throws TransactionException;

  /**
   * Rolls the transaction back. This method is idempotent and thread-safe. It will do nothing if this method
   * or {@link #commit()} have already been invoked on {@code this} instance.
   *
   * @throws TransactionException if the transaction fails to roll back
   */
  void rollback() throws TransactionException;

  /**
   * If the actual transaction is marked as RollbackOnly, then rollbacks the transaction. In other case, commit
   * the transaction
   * @throws TransactionException
   */
  void resolve() throws TransactionException;
}
