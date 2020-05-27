/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for the chain of components defined through a DSL.
 *
 * @since 1.4
 */
public final class ChainStereotype extends MuleStereotypeDefinition {

  ChainStereotype() {}

  @Override
  public String getName() {
    return "CHAIN";
  }

}
