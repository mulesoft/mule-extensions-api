/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
