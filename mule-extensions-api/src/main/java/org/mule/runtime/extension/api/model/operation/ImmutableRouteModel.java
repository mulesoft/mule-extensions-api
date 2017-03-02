/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.operation;

import static java.util.Optional.ofNullable;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.Stereotype;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.operation.RouteModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.AbstractParameterizedModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class ImmutableRouteModel extends AbstractParameterizedModel implements RouteModel {

  private int minOccurs;
  private Integer maxOccurs;
  private Set<Stereotype> allowedStereotypes;


  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the component's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public ImmutableRouteModel(String name,
                             String description,
                             int minOccurs,
                             Integer maxOccurs,
                             Set<Stereotype> allowedStereotypes,
                             List<ParameterGroupModel> parameterGroupModels,
                             DisplayModel displayModel,
                             Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
    checkArgument(minOccurs >= 0, "minOccurs has to be greater or equal to zero");

    if (maxOccurs != null) {
      checkArgument(maxOccurs >= 1, "maxOccurs has to be greater or equal to 1");
    }

    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
    this.allowedStereotypes = allowedStereotypes != null && !allowedStereotypes.isEmpty() ? copy(allowedStereotypes) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinOccurs() {
    return minOccurs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Integer> getMaxOccurs() {
    return ofNullable(maxOccurs);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Set<Stereotype>> getAllowedStereotypes() {
    return ofNullable(allowedStereotypes);
  }
}
