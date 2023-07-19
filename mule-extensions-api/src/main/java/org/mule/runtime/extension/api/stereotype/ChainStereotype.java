/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
