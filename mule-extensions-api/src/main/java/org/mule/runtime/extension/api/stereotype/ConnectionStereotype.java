/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any generic connection.
 *
 * @since 1.0
 */
public final class ConnectionStereotype extends MuleStereotypeDefinition {

  ConnectionStereotype() {}

  @Override
  public String getName() {
    return "CONNECTION";
  }

}
