/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.tx.TransactionType;

/**
 * Marker {@link ModelProperty} to indicate that the enriched element is a {@link TransactionType}
 *
 * @since 1.0
 */
public class TransactionalTypeModelProperty implements ModelProperty {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "transactionType";
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
