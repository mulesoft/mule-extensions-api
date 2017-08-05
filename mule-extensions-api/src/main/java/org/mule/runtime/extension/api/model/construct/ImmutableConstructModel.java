/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.construct;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class ImmutableConstructModel extends AbstractComponentModel implements ConstructModel {

  private final boolean allowsTopLevelDefinition;

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param stereotypes          A {@link Set} of {@link StereotypeDefinition stereotypes}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param allowsTopLevelDefinition  whether or not {@code this} model can be declared as a root component in the application
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableConstructModel(String name,
                                 String description,
                                 List<ParameterGroupModel> parameterGroupModels,
                                 DisplayModel displayModel,
                                 Set<StereotypeModel> stereotypes,
                                 Set<ModelProperty> modelProperties,
                                 List<? extends NestableElementModel> nestedComponents,
                                 boolean allowsTopLevelDefinition) {
    super(name, description, parameterGroupModels, displayModel, stereotypes, modelProperties, nestedComponents);
    this.allowsTopLevelDefinition = allowsTopLevelDefinition;
  }

  @Override
  public boolean allowsTopLevelDeclaration() {
    return allowsTopLevelDefinition;
  }

}
