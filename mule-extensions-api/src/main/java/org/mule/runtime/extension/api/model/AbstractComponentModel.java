/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.parameter.AbstractParameterizedModel;

import java.util.List;
import java.util.Set;


/**
 * Base class for immutable implementations of a {@link ComponentModel}
 *
 * @since 1.0
 */
public abstract class AbstractComponentModel extends AbstractParameterizedModel
    implements ComponentModel {

  private final Set<StereotypeModel> stereotypes;
  private final List<? extends NestableElementModel> nestedComponents;

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotypes          A {@link Set} of {@link StereotypeModel stereotypes}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   DisplayModel displayModel,
                                   Set<StereotypeModel> stereotypes,
                                   Set<ModelProperty> modelProperties,
                                   List<? extends NestableElementModel> nestedComponents) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);

    this.stereotypes = copy(stereotypes);
    this.nestedComponents = copy(nestedComponents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<StereotypeModel> getStereotypes() {
    return stereotypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<? extends NestableElementModel> getNestedComponents() {
    return nestedComponents;
  }
}
