/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for a generic {@link Object} definition
 *
 * @since 1.3
 */
public class ObjectStereotype extends MuleStereotypeDefinition {

  @Override
  public String getName() {
    return "OBJECT";
  }

}
