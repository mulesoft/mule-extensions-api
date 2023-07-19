/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.runtime.parameter.Literal;

/**
 * Marker {@link ModelProperty} for {@link ParameterModel}s that indicates that the enriched parameter is considered as a Target
 * type. This is required to handle the parameter as a {@link Literal} one.
 *
 * @since 1.0
 */
public class TargetModelProperty implements ModelProperty {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "target";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
