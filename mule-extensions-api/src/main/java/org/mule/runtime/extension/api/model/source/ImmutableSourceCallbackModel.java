/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.source;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.extension.api.model.parameter.AbstractParameterizedModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link SourceCallbackModel}
 *
 * @since 1.0
 */
public class ImmutableSourceCallbackModel extends AbstractParameterizedModel implements SourceCallbackModel {

  /**
   * Creates a new instance
   *
   * @param name the model's name
   * @param description the model's description
   * @param parameterGroupModels a {@link List} with the component's {@link ParameterGroupModel parameter group models}
   * @param displayModel a model which contains directive about how this component is displayed in the UI
   * @param modelProperties A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public ImmutableSourceCallbackModel(String name, String description,
                                      List<ParameterGroupModel> parameterGroupModels,
                                      DisplayModel displayModel,
                                      Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
  }
}
