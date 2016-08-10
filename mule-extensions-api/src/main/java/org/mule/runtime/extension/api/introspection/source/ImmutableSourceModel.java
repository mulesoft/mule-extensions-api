/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.source;

import org.mule.runtime.extension.api.introspection.AbstractComponentModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.OutputModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class ImmutableSourceModel extends AbstractComponentModel implements SourceModel {

  /**
   * Creates a new instance
   *
   * @param name             the source name. Cannot be blank
   * @param description      the source description
   * @param parameterModels  a {@link List} with the source's {@link ParameterModel parameterModels}
   * @param output           an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes an {@link OutputModel} which represents the attributes on the output me
   * @param modelProperties  A {@link Set} of custom properties which extend this model
   */
  public ImmutableSourceModel(String name,
                              String description,
                              List<ParameterModel> parameterModels,
                              OutputModel output,
                              OutputModel outputAttributes,
                              Set<ModelProperty> modelProperties) {
    super(name, description, modelProperties, parameterModels, output, outputAttributes);
  }

}
