/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any generic processor that can be executed by mule runtime.
 *
 * @since 1.0
 */
public final class ProcessorStereotype extends MuleStereotypeDefinition {

  ProcessorStereotype() {}

  @Override
  public String getName() {
    return "PROCESSOR";
  }

}
