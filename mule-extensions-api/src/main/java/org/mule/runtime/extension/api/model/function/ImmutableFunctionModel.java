/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.function;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.AbstractParameterizedModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * Immutable concrete implementation of {@link FunctionModel}
 *
 * @since 1.0
 */
public class ImmutableFunctionModel extends AbstractParameterizedModel implements FunctionModel {

  private final OutputModel output;
  private final DeprecationModel deprecationModel;

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableFunctionModel(String name,
                                String description,
                                List<ParameterGroupModel> parameterGroupModels,
                                OutputModel output,
                                DisplayModel displayModel,
                                Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, output, displayModel, modelProperties, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the function is deprecated. A null value means it is not
   *                             deprecated.
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableFunctionModel(String name,
                                String description,
                                List<ParameterGroupModel> parameterGroupModels,
                                OutputModel output,
                                DisplayModel displayModel,
                                Set<ModelProperty> modelProperties,
                                DeprecationModel deprecationModel) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
    this.output = output;
    this.deprecationModel = deprecationModel;
  }

  @Override
  public OutputModel getOutput() {
    return output;
  }

  @Override
  public Optional<DeprecationModel> getDeprecationModel() {
    return ofNullable(deprecationModel);
  }

  @Override
  public boolean isDeprecated() {
    return deprecationModel != null;
  }
}
