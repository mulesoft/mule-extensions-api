/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for {@code sub-flow} core components
 *
 * @since 1.3
 */
public final class SubFlowStereotype extends MuleStereotypeDefinition {

  SubFlowStereotype() {}

  @Override
  public String getName() {
    return "SUB_FLOW";
  }

}
