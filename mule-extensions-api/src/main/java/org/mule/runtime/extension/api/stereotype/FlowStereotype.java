/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for {@code flow} core components
 *
 * @since 1.0
 */
public final class FlowStereotype extends MuleStereotypeDefinition {

  FlowStereotype() {}

  @Override
  public String getName() {
    return "FLOW";
  }

}
