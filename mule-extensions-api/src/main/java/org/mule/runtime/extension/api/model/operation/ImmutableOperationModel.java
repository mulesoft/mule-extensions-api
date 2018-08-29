/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.operation;

import static java.util.Collections.emptySet;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.operation.ExecutionType;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractExecutableComponentModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class ImmutableOperationModel extends AbstractExecutableComponentModel implements OperationModel {

  private final boolean blocking;
  private final ExecutionType executionType;

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param blocking             whether this operation executes in a blocking manner
   * @param executionType        describes the type of processing this operation performs
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param errors               A {@link Set} with all the {@link ErrorModel} that are declared to be thrown by
   *                             the operation
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   * @deprecated This constructor is deprecated and will be removed in Mule 5. Use
   * {@link #ImmutableOperationModel(String, String, List, List, OutputModel, OutputModel, boolean, ExecutionType, boolean, boolean, boolean, DisplayModel, Set, StereotypeModel, Set, Set)}
   * instead
   */
  @Deprecated
  public ImmutableOperationModel(String name,
                                 String description,
                                 List<ParameterGroupModel> parameterGroupModels,
                                 List<? extends NestableElementModel> nestedComponents, OutputModel output,
                                 OutputModel outputAttributes,
                                 boolean blocking,
                                 ExecutionType executionType,
                                 boolean requiresConnection,
                                 boolean transactional,
                                 boolean supportsStreaming,
                                 DisplayModel displayModel,
                                 Set<ErrorModel> errors,
                                 StereotypeModel stereotype,
                                 Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, nestedComponents, output, outputAttributes, blocking, executionType,
         requiresConnection, transactional, supportsStreaming, displayModel, errors, stereotype, modelProperties, emptySet());
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param blocking             whether this operation executes in a blocking manner
   * @param executionType        describes the type of processing this operation performs
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param errors               A {@link Set} with all the {@link ErrorModel} that are declared to be thrown by
   *                             the operation
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param notifications        A {@link Set} of {@link NotificationModel} which describes the operation's notifications
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableOperationModel(String name,
                                 String description,
                                 List<ParameterGroupModel> parameterGroupModels,
                                 List<? extends NestableElementModel> nestedComponents, OutputModel output,
                                 OutputModel outputAttributes,
                                 boolean blocking,
                                 ExecutionType executionType,
                                 boolean requiresConnection,
                                 boolean transactional,
                                 boolean supportsStreaming,
                                 DisplayModel displayModel,
                                 Set<ErrorModel> errors,
                                 StereotypeModel stereotype,
                                 Set<ModelProperty> modelProperties,
                                 Set<NotificationModel> notifications) {
    this(name, description, parameterGroupModels, nestedComponents, output, outputAttributes, blocking, executionType,
         requiresConnection, transactional, supportsStreaming, displayModel, errors, stereotype, modelProperties, notifications,
         null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param blocking             whether this operation executes in a blocking manner
   * @param executionType        describes the type of processing this operation performs
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param errors               A {@link Set} with all the {@link ErrorModel} that are declared to be thrown by
   *                             the operation
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param notifications        A {@link Set} of {@link NotificationModel} which describes the operation's notifications
   * @param deprecationModel     a {@link DeprecationModel} describing if the operation is deprecated. A null value means it is not
   *                             deprecated.
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableOperationModel(String name,
                                 String description,
                                 List<ParameterGroupModel> parameterGroupModels,
                                 List<? extends NestableElementModel> nestedComponents, OutputModel output,
                                 OutputModel outputAttributes,
                                 boolean blocking,
                                 ExecutionType executionType,
                                 boolean requiresConnection,
                                 boolean transactional,
                                 boolean supportsStreaming,
                                 DisplayModel displayModel,
                                 Set<ErrorModel> errors,
                                 StereotypeModel stereotype,
                                 Set<ModelProperty> modelProperties,
                                 Set<NotificationModel> notifications,
                                 DeprecationModel deprecationModel) {
    super(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional, supportsStreaming,
          displayModel, errors, stereotype, modelProperties, nestedComponents, notifications, deprecationModel);
    this.blocking = blocking;
    this.executionType = executionType;

  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBlocking() {
    return blocking;
  }

  /**
   * {@inheritDoc}
   */
  public ExecutionType getExecutionType() {
    return executionType;
  }

  @Override
  public String toString() {
    return getName() + " {" + super.toString() + "}";
  }
}
