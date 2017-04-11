/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.operation;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.ExecutionType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.Stereotype;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.operation.RouteModel;
import org.mule.runtime.api.meta.model.operation.ScopeModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link ScopeModel}
 *
 * @since 1.0
 */
public final class ImmutableScopeModel extends ImmutableOperationModel implements ScopeModel {

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the scope's name. Cannot be blank
   * @param description          the scope's descriptor
   * @param parameterGroupModels a {@link List} with the scope's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the scope's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param blocking             whether this operation executes in a blocking manner
   * @param executionType        describes the type of processing this operation performs
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param errors               A {@link Set} with all the {@link ErrorModel} that are declared to be thrown by
   *                             the operation
   * @param stereotypes          A {@link Set} of {@link Stereotype stereotypes}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableScopeModel(String name, String description,
                             List<ParameterGroupModel> parameterGroupModels,
                             OutputModel output,
                             OutputModel outputAttributes,
                             boolean blocking,
                             ExecutionType executionType,
                             boolean requiresConnection,
                             boolean transactional,
                             boolean supportsStreaming,
                             DisplayModel displayModel,
                             Set<ErrorModel> errors,
                             Set<Stereotype> stereotypes,
                             Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, output, outputAttributes, blocking, executionType, requiresConnection,
          transactional, supportsStreaming, displayModel, errors, stereotypes, modelProperties);
  }

}
