/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecableModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.HasStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for immutable implementation of {@link HasStereotypeModel}
 *
 * @since 1.0
 */
public abstract class AbstractStereotypedModel extends AbstractParameterizedModel
    implements HasStereotypeModel, DeprecableModel {

  private final StereotypeModel stereotype;
  private final DeprecationModel deprecationModel;

  public AbstractStereotypedModel(String name, String description,
                                  List<ParameterGroupModel> parameterGroupModels,
                                  DisplayModel displayModel,
                                  StereotypeModel stereotype,
                                  Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, displayModel, stereotype, modelProperties, null);
  }

  public AbstractStereotypedModel(String name, String description,
                                  List<ParameterGroupModel> parameterGroupModels,
                                  DisplayModel displayModel,
                                  StereotypeModel stereotype,
                                  Set<ModelProperty> modelProperties,
                                  DeprecationModel deprecationModel) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
    this.stereotype = stereotype;
    this.deprecationModel = deprecationModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StereotypeModel getStereotype() {
    return stereotype;
  }

  @Override
  public Optional<DeprecationModel> getDeprecationModel() {
    return Optional.ofNullable(deprecationModel);
  }

  @Override
  public boolean isDeprecated() {
    return deprecationModel != null;
  }
}
