/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.source;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractExecutableComponentModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class ImmutableSourceModel extends AbstractExecutableComponentModel implements SourceModel {

  private final boolean hasResponse;
  private final boolean runsOnPrimaryNodeOnly;
  private final SourceCallbackModel successCallback;
  private final SourceCallbackModel errorCallback;
  private final SourceCallbackModel terminateCallbackModel;

  /**
   * Creates a new instance
   *
   * @param name                 the source name. Cannot be blank
   * @param description          the source description
   * @param hasResponse          Whether the source emits a response
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output me
   * @param successCallbackModel an optional model for the source success callback
   * @param errorCallbackModel   an optional model for the source error callback
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this source is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @deprecated This constructor is deprecated and will be removed in Mule 5. Use
   * {@link #ImmutableSourceModel(String, String, boolean, boolean, List, List, OutputModel, OutputModel, Optional, Optional, Optional, boolean, boolean, boolean, DisplayModel, StereotypeModel, Set, Set, Set)}
   * instead
   */
  @Deprecated
  public ImmutableSourceModel(String name,
                              String description,
                              boolean hasResponse,
                              List<ParameterGroupModel> parameterGroupModels,
                              List<? extends NestableElementModel> nestedComponents, OutputModel output,
                              OutputModel outputAttributes,
                              Optional<SourceCallbackModel> successCallbackModel,
                              Optional<SourceCallbackModel> errorCallbackModel,
                              Optional<SourceCallbackModel> terminateCallbackModel,
                              boolean requiresConnection,
                              boolean transactional,
                              boolean supportsStreaming,
                              DisplayModel displayModel,
                              StereotypeModel stereotype,
                              Set<ErrorModel> errors,
                              Set<ModelProperty> modelProperties) {
    this(name, description, hasResponse, false, parameterGroupModels, nestedComponents, output, outputAttributes,
         successCallbackModel, errorCallbackModel, terminateCallbackModel, requiresConnection, transactional, supportsStreaming,
         displayModel, stereotype, errors, modelProperties, emptySet());
  }

  /**
   * Creates a new instance
   *
   * @param name                  the source name. Cannot be blank
   * @param description           the source description
   * @param hasResponse           Whether the source emits a response
   * @param runsOnPrimaryNodeOnly Whether the source should only run on the primary node or all nodes
   * @param parameterGroupModels  a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents      a {@link List} with the components contained by this model
   * @param output                an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes      an {@link OutputModel} which represents the attributes on the output me
   * @param successCallbackModel  an optional model for the source success callback
   * @param errorCallbackModel    an optional model for the source error callback
   * @param requiresConnection    whether this component requires connectivity
   * @param transactional         whether this component supports transactions
   * @param supportsStreaming     whether this component supports streaming
   * @param displayModel          a model which contains directive about how this source is displayed in the UI
   * @param stereotype            the {@link StereotypeModel stereotype} of this component
   * @param modelProperties       A {@link Set} of custom properties which extend this model
   * @param notifications         A {@link Set} of {@link NotificationModel} which describes the source's notifications
   */
  public ImmutableSourceModel(String name,
                              String description,
                              boolean hasResponse,
                              boolean runsOnPrimaryNodeOnly,
                              List<ParameterGroupModel> parameterGroupModels,
                              List<? extends NestableElementModel> nestedComponents, OutputModel output,
                              OutputModel outputAttributes,
                              Optional<SourceCallbackModel> successCallbackModel,
                              Optional<SourceCallbackModel> errorCallbackModel,
                              Optional<SourceCallbackModel> terminateCallbackModel,
                              boolean requiresConnection,
                              boolean transactional,
                              boolean supportsStreaming,
                              DisplayModel displayModel,
                              StereotypeModel stereotype,
                              Set<ErrorModel> errors,
                              Set<ModelProperty> modelProperties,
                              Set<NotificationModel> notifications) {
    super(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional,
          supportsStreaming, displayModel, errors, stereotype, modelProperties, nestedComponents, notifications);
    this.hasResponse = hasResponse;
    this.runsOnPrimaryNodeOnly = runsOnPrimaryNodeOnly;
    this.successCallback = successCallbackModel.orElse(null);
    this.errorCallback = errorCallbackModel.orElse(null);
    this.terminateCallbackModel = terminateCallbackModel.orElse(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasResponse() {
    return hasResponse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<SourceCallbackModel> getSuccessCallback() {
    return ofNullable(successCallback);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<SourceCallbackModel> getErrorCallback() {
    return ofNullable(errorCallback);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<SourceCallbackModel> getTerminateCallback() {
    return ofNullable(terminateCallbackModel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean runsOnPrimaryNodeOnly() {
    return runsOnPrimaryNodeOnly;
  }
}
