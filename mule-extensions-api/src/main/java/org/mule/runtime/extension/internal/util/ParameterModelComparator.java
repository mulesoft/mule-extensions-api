/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;

import java.util.Comparator;

/**
 * A {@link Comparator} for sorting objects of type {@link ParameterModel}
 */
public class ParameterModelComparator implements Comparator<ParameterModel> {

  private final boolean infrastructureFirst;

  /**
   * Creates a new instance
   *
   * @param infrastructureFirst whether infrastructure parameters should be at the beginning or the end
   */
  public ParameterModelComparator(boolean infrastructureFirst) {
    this.infrastructureFirst = infrastructureFirst;
  }

  @Override
  public int compare(ParameterModel left, ParameterModel right) {
    boolean isLeftInfrastructure = left.getModelProperty(InfrastructureParameterModelProperty.class).isPresent();
    boolean isRightInfrastructure = right.getModelProperty(InfrastructureParameterModelProperty.class).isPresent();

    if (!isLeftInfrastructure && !isRightInfrastructure) {
      return 0;
    }

    if (isLeftInfrastructure) {
      return infrastructureFirst ? -1 : 1;
    }

    if (isRightInfrastructure) {
      return infrastructureFirst ? 1 : -1;
    }

    return left.getName().compareTo(right.getName());
  }
}
