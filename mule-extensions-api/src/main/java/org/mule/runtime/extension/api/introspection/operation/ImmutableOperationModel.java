/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.operation;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.introspection.AbstractComponentModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.OutputModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class ImmutableOperationModel extends AbstractComponentModel implements OperationModel {

  /**
   * Creates a new instance with the given state
   *
   * @param name             the operation's name. Cannot be blank
   * @param description      the operation's descriptor
   * @param parameterModels  a {@link List} with the operation's {@link ParameterModel parameterModels}
   * @param output           an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param modelProperties  A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableOperationModel(String name,
                                 String description,
                                 List<ParameterModel> parameterModels,
                                 OutputModel output,
                                 OutputModel outputAttributes,
                                 Set<ModelProperty> modelProperties) {
    super(name, description, modelProperties, parameterModels, output, outputAttributes);
  }
}
