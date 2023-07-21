/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} for indicating that the owning operation may not have a configuring streaming strategy, regardless of it
 * being a streaming operation.
 *
 * @since 1.5
 */
public class NoStreamingConfigurationModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "noStreamingConfiguration";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
