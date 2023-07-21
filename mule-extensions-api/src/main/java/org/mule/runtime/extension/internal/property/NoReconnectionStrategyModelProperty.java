/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.api.ExtensionConstants;

/**
 * Marker {@link ModelProperty} for XML modules to determine the {@link ExtensionConstants#RECONNECTION_CONFIG_PARAMETER_NAME}
 * element must not be added.
 *
 * @since 1.0
 */
public class NoReconnectionStrategyModelProperty implements ModelProperty {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "noReconnectionStrategy";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
