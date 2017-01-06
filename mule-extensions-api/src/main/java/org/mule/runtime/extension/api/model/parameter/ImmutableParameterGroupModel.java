/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import static java.util.Optional.ofNullable;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ExclusiveParametersModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.model.AbstractNamedImmutableModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link ParameterGroupModel}
 *
 * @since 1.0
 */
public final class ImmutableParameterGroupModel extends AbstractNamedImmutableModel implements ParameterGroupModel {

  private final List<ParameterModel> parameters;
  private final List<ExclusiveParametersModel> exclusiveParametersModels;
  private final LayoutModel layoutModel;
  private final boolean showInDsl;

  /**
   * Creates a new instance
   *
   * @param name                      the operation's name. Cannot be blank
   * @param description               the operation's descriptor
   * @param parameters                the parameters contained in this group
   * @param exclusiveParametersModels a list with the applying {@link ExclusiveParametersModel}
   * @param showInDsl                if {@code true}, {@code this} Group will be shown as an inline element of the DSL
   * @param displayModel              a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel               a model which contains directives about the parameter's layout in the UI
   * @param modelProperties           A {@link Set} of custom properties which extend this model
   */
  public ImmutableParameterGroupModel(String name,
                                      String description,
                                      List<ParameterModel> parameters,
                                      List<ExclusiveParametersModel> exclusiveParametersModels,
                                      boolean showInDsl,
                                      DisplayModel displayModel,
                                      LayoutModel layoutModel,
                                      Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, modelProperties);
    this.parameters = copy(parameters);
    this.exclusiveParametersModels = copy(exclusiveParametersModels);
    this.layoutModel = layoutModel;
    this.showInDsl = showInDsl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ParameterModel> getParameterModels() {
    return parameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ExclusiveParametersModel> getExclusiveParametersModels() {
    return exclusiveParametersModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<LayoutModel> getLayoutModel() {
    return ofNullable(layoutModel);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isShowInDsl() {
    return showInDsl;
  }

}
