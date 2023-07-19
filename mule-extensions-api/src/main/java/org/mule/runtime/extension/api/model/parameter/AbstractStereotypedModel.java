/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.model.parameter;

import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecableModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.HasStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.api.meta.model.version.HasMinMuleVersion;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for immutable implementation of {@link HasStereotypeModel}
 *
 * @since 1.0
 */
public abstract class AbstractStereotypedModel extends AbstractParameterizedModel
    implements HasStereotypeModel, DeprecableModel, HasMinMuleVersion {

  private final StereotypeModel stereotype;
  private final DeprecationModel deprecationModel;
  private final MuleVersion minMuleVersion;

  public AbstractStereotypedModel(String name, String description,
                                  List<ParameterGroupModel> parameterGroupModels,
                                  DisplayModel displayModel,
                                  StereotypeModel stereotype,
                                  Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, displayModel, stereotype, modelProperties, null);
  }

  public AbstractStereotypedModel(String name, String description,
                                  List<ParameterGroupModel> parameterGroupModels,
                                  DisplayModel displayModel,
                                  StereotypeModel stereotype,
                                  Set<ModelProperty> modelProperties,
                                  DeprecationModel deprecationModel) {
    this(name, description, parameterGroupModels, displayModel, stereotype, modelProperties, deprecationModel, null);
  }

  public AbstractStereotypedModel(String name, String description,
                                  List<ParameterGroupModel> parameterGroupModels,
                                  DisplayModel displayModel,
                                  StereotypeModel stereotype,
                                  Set<ModelProperty> modelProperties,
                                  DeprecationModel deprecationModel,
                                  MuleVersion minMuleVersion) {
    super(name, description, parameterGroupModels, displayModel, modelProperties);
    this.stereotype = stereotype;
    this.deprecationModel = deprecationModel;
    this.minMuleVersion = minMuleVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StereotypeModel getStereotype() {
    return stereotype;
  }

  @Override
  public Optional<DeprecationModel> getDeprecationModel() {
    return ofNullable(deprecationModel);
  }

  @Override
  public boolean isDeprecated() {
    return deprecationModel != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<MuleVersion> getMinMuleVersion() {
    return ofNullable(minMuleVersion);
  }

  @Override
  public String toString() {
    return "AbstractStereotypedModel{" +
        "stereotype=" + stereotype +
        ", deprecationModel=" + deprecationModel +
        ", minMuleVersion=" + minMuleVersion +
        "} " + super.toString();
  }
}
