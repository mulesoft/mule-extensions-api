/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.Stereotype;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.AbstractParameterizedModel;

import java.util.List;
import java.util.Set;


/**
 * Base class for immutable implementations of a {@link ComponentModel}
 *
 * @since 1.0
 */
public abstract class AbstractComponentModel<T extends ComponentModel> extends AbstractParameterizedModel
    implements ComponentModel<T> {

  private final OutputModel output;
  private final OutputModel outputAttributes;
  private final boolean transactional;
  private final boolean requiresConnection;
  private final Set<Stereotype> stereotypes;

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the component's output content
   * @param outputAttributes     an {@link OutputModel} which represents the component's attributes on the output {@link Message}
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotypes          A {@link Set} of {@link Stereotype stereotypes}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   OutputModel output,
                                   OutputModel outputAttributes,
                                   boolean requiresConnection,
                                   boolean transactional,
                                   DisplayModel displayModel,
                                   Set<Stereotype> stereotypes,
                                   Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
    this.output = output;
    this.outputAttributes = outputAttributes;
    this.requiresConnection = requiresConnection;
    this.transactional = transactional;
    this.stereotypes = copy(stereotypes);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTransactional() {
    return transactional;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean requiresConnection() {
    return requiresConnection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Stereotype> getStereotypes() {
    return stereotypes;
  }
}
