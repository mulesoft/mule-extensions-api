/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
