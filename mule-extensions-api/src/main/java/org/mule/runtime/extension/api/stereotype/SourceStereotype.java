/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any generic source that can be executed by mule runtime.
 *
 * @since 1.0
 */
public final class SourceStereotype extends MuleStereotypeDefinition {

  SourceStereotype() {}

  @Override
  public String getName() {
    return "SOURCE";
  }

}
