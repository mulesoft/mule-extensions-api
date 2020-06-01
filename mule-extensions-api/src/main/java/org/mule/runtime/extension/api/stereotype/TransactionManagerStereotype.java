/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any transaction manager global element.
 *
 * @since 1.4, 1.3.1
 */
public final class TransactionManagerStereotype extends MuleStereotypeDefinition {

  TransactionManagerStereotype() {}

  @Override
  public String getName() {
    return "TX_MANAGER";
  }

}
