/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for {@code on-error} core components
 *
 * @since 1.1
 */
public final class OnErrorStereotype extends MuleStereotypeDefinition {

  OnErrorStereotype() {}

  @Override
  public String getName() {
    return "ON_ERROR";
  }

}
