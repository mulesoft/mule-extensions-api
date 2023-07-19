/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for {@code serializer} components
 *
 * @since 1.1
 */
public final class SerializerStereotype extends MuleStereotypeDefinition {

  SerializerStereotype() {}

  @Override
  public String getName() {
    return "SERIALIZER";
  }

}
