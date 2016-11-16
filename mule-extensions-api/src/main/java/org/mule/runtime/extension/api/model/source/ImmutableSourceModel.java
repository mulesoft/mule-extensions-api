/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.source;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class ImmutableSourceModel extends AbstractComponentModel implements SourceModel {

  private final boolean hasResponse;

  /**
   * Creates a new instance
   *
   * @param name                 the source name. Cannot be blank
   * @param description          the source description
   * @param hasResponse          Whether the source emits a response
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output me
   * @param displayModel         a model which contains directive about how this source is displayed in the UI
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   */
  public ImmutableSourceModel(String name,
                              String description,
                              boolean hasResponse,
                              List<ParameterGroupModel> parameterGroupModels,
                              OutputModel output,
                              OutputModel outputAttributes,
                              DisplayModel displayModel,
                              Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, modelProperties, parameterGroupModels, output, outputAttributes);
    this.hasResponse = hasResponse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasResponse() {
    return hasResponse;
  }
}
