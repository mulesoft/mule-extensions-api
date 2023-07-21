/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for {@code error-handler} core components
 *
 * @since 1.0
 */
public final class ErrorHandlerStereotype extends MuleStereotypeDefinition {

  ErrorHandlerStereotype() {}

  @Override
  public String getName() {
    return "ERROR_HANDLER";
  }

}
