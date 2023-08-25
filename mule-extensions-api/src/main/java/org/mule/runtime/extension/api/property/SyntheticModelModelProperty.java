/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} to be used on any {@link org.mule.runtime.api.meta.model.EnrichableModel} that it's added to to a
 * {@link org.mule.runtime.api.meta.model.ExtensionModel} by the Mule's Sdk.
 *
 * @since 1.2.0
 */
public class SyntheticModelModelProperty implements ModelProperty {

  public static final String NAME = "syntheticModel";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
