/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.source;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.resolveOutputModelType;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.resolveParameterGroupModelType;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.resolveSourceCallbackType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.metadata.descriptor.InputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class ImmutableSourceModel extends AbstractComponentModel<SourceModel> implements SourceModel {

  private final boolean hasResponse;
  private final SourceCallbackModel successCallback;
  private final SourceCallbackModel errorCallback;

  /**
   * Creates a new instance
   *
   * @param name                 the source name. Cannot be blank
   * @param description          the source description
   * @param hasResponse          Whether the source emits a response
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output me
   * @param successCallbackModel an optional model for the source success callback
   * @param errorCallbackModel   an optional model for the source error callback
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param displayModel         a model which contains directive about how this source is displayed in the UI
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   */
  public ImmutableSourceModel(String name,
                              String description,
                              boolean hasResponse,
                              List<ParameterGroupModel> parameterGroupModels,
                              OutputModel output,
                              OutputModel outputAttributes,
                              Optional<SourceCallbackModel> successCallbackModel,
                              Optional<SourceCallbackModel> errorCallbackModel,
                              boolean requiresConnection,
                              boolean transactional,
                              DisplayModel displayModel,
                              Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional, displayModel,
          modelProperties);
    this.hasResponse = hasResponse;
    this.successCallback = successCallbackModel.orElse(null);
    this.errorCallback = errorCallbackModel.orElse(null);
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
  public Set<ErrorModel> getErrorModels() {
    return emptySet();
  }

  @Override
  public SourceModel getTypedModel(InputMetadataDescriptor inputMetadataDescriptor,
                                   OutputMetadataDescriptor outputMetadataDescriptor) {
    OutputModel typedOutputModel = resolveOutputModelType(getOutput(), outputMetadataDescriptor.getPayloadMetadata());
    OutputModel typedAttributesModel =
        resolveOutputModelType(getOutputAttributes(), outputMetadataDescriptor.getAttributesMetadata());

    return new ImmutableSourceModel(getName(), getDescription(), hasResponse(),
                                    resolveParameterGroupModelType(getParameterGroupModels(),
                                                                   inputMetadataDescriptor
                                                                       .getAllParameters()),
                                    typedOutputModel, typedAttributesModel,
                                    resolveSourceCallbackType(getSuccessCallback(),
                                                              inputMetadataDescriptor.getAllParameters()),
                                    resolveSourceCallbackType(getErrorCallback(),
                                                              inputMetadataDescriptor.getAllParameters()),
                                    requiresConnection(),
                                    isTransactional(),
                                    getDisplayModel().orElse(null),
                                    getModelProperties());

  }
}
