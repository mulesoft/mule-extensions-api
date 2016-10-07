/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.extension.api.introspection.parameter.AbstractParameterizedModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

import java.util.List;
import java.util.Set;


/**
 * Base class for immutable implementations of a {@link ComponentModel}
 *
 * @since 1.0
 */
public abstract class AbstractComponentModel extends AbstractParameterizedModel implements ComponentModel {

  private final OutputModel output;
  private final OutputModel outputAttributes;

  /**
   * Creates a new instance
   *
   * @param name             the model's name
   * @param description      the model's description
   * @param displayModel     a model which contains directive about how this component is displayed in the UI
   * @param modelProperties  A {@link Set} of custom properties which extend this model
   * @param parameterModels  a {@link List} with the source's {@link ParameterModel parameterModels}
   * @param output           an {@link OutputModel} which represents the component's output content
   * @param outputAttributes an {@link OutputModel} which represents the component's attributes on the output {@link Message}
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   DisplayModel displayModel,
                                   Set<ModelProperty> modelProperties,
                                   List<ParameterModel> parameterModels,
                                   OutputModel output,
                                   OutputModel outputAttributes) {
    super(name, description, displayModel, modelProperties, parameterModels);
    this.output = output;
    this.outputAttributes = outputAttributes;
  }

  /**
   * {@inheritDoc}
   */
  public OutputModel getOutput() {
    return output;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputModel getOutputAttributes() {
    return outputAttributes;
  }
}
