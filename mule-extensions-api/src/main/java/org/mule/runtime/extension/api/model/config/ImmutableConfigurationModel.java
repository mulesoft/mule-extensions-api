/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.config;

import static java.util.Collections.min;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractComplexModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ImmutableConfigurationModel extends AbstractComplexModel implements ConfigurationModel {

  private final StereotypeModel stereotype;
  private final List<ParameterGroupModel> parameterGroupModels;
  private final Set<ExternalLibraryModel> externalLibraryModels;
  private final MuleVersion minMuleVersion;

  /**
   * Creates a new instance with the given state
   *
   * @param name                  the configuration's name
   * @param description           the configuration's description
   * @param parameterGroupModels  a {@link List} with the configuration's {@link ParameterGroupModel parameter group models}
   * @param operationModels       a {@link List} with the configuration's {@link OperationModel operationModels}
   * @param connectionProviders   a {@link List} with the configuration's {@link ConnectionProviderModel connection provider
   *                              models}
   * @param sourceModels          a {@link List} with the configuration's {@link SourceModel message source models}
   * @param externalLibraryModels a {@link Set} with the configuration's {@link ExternalLibraryModel external libraries}
   * @param displayModel          a model which contains directive about how this configuration is displayed in the UI
   * @param modelProperties       a {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
   */
  public ImmutableConfigurationModel(String name,
                                     String description,
                                     List<ParameterGroupModel> parameterGroupModels,
                                     List<OperationModel> operationModels,
                                     List<ConnectionProviderModel> connectionProviders,
                                     List<SourceModel> sourceModels,
                                     Set<ExternalLibraryModel> externalLibraryModels,
                                     DisplayModel displayModel,
                                     StereotypeModel stereotype,
                                     Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, operationModels, connectionProviders, sourceModels, externalLibraryModels,
         displayModel, stereotype, modelProperties, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                  the configuration's name
   * @param description           the configuration's description
   * @param parameterGroupModels  a {@link List} with the configuration's {@link ParameterGroupModel parameter group models}
   * @param operationModels       a {@link List} with the configuration's {@link OperationModel operationModels}
   * @param connectionProviders   a {@link List} with the configuration's {@link ConnectionProviderModel connection provider
   *                              models}
   * @param sourceModels          a {@link List} with the configuration's {@link SourceModel message source models}
   * @param externalLibraryModels a {@link Set} with the configuration's {@link ExternalLibraryModel external libraries}
   * @param displayModel          a model which contains directive about how this configuration is displayed in the UI
   * @param modelProperties       a {@link Set} of custom properties which extend this model
   * @param deprecationModel      a {@link DeprecationModel} describing if the configuration is deprecated. A null value means it
   *                              is not deprecated.
   * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
   */
  public ImmutableConfigurationModel(String name,
                                     String description,
                                     List<ParameterGroupModel> parameterGroupModels,
                                     List<OperationModel> operationModels,
                                     List<ConnectionProviderModel> connectionProviders,
                                     List<SourceModel> sourceModels,
                                     Set<ExternalLibraryModel> externalLibraryModels,
                                     DisplayModel displayModel,
                                     StereotypeModel stereotype,
                                     Set<ModelProperty> modelProperties,
                                     DeprecationModel deprecationModel) {
    this(name, description, parameterGroupModels, operationModels, connectionProviders, sourceModels, externalLibraryModels,
         displayModel, stereotype, modelProperties, deprecationModel, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                  the configuration's name
   * @param description           the configuration's description
   * @param parameterGroupModels  a {@link List} with the configuration's {@link ParameterGroupModel parameter group models}
   * @param operationModels       a {@link List} with the configuration's {@link OperationModel operationModels}
   * @param connectionProviders   a {@link List} with the configuration's {@link ConnectionProviderModel connection provider
   *                              models}
   * @param sourceModels          a {@link List} with the configuration's {@link SourceModel message source models}
   * @param externalLibraryModels a {@link Set} with the configuration's {@link ExternalLibraryModel external libraries}
   * @param displayModel          a model which contains directive about how this configuration is displayed in the UI
   * @param modelProperties       a {@link Set} of custom properties which extend this model
   * @param deprecationModel      a {@link DeprecationModel} describing if the configuration is deprecated. A null value means it
   *                              is not deprecated.
   * @param minMuleVersion        the min mule version of the configuration
   * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
   * @since 1.6
   */
  public ImmutableConfigurationModel(String name,
                                     String description,
                                     List<ParameterGroupModel> parameterGroupModels,
                                     List<OperationModel> operationModels,
                                     List<ConnectionProviderModel> connectionProviders,
                                     List<SourceModel> sourceModels,
                                     Set<ExternalLibraryModel> externalLibraryModels,
                                     DisplayModel displayModel,
                                     StereotypeModel stereotype,
                                     Set<ModelProperty> modelProperties,
                                     DeprecationModel deprecationModel,
                                     MuleVersion minMuleVersion) {
    super(name, description, operationModels, connectionProviders, sourceModels, displayModel, modelProperties, deprecationModel);
    this.parameterGroupModels = unmodifiableList(new ArrayList<>(parameterGroupModels));
    this.externalLibraryModels = unmodifiableSet(externalLibraryModels);
    this.stereotype = stereotype;
    this.minMuleVersion = minMuleVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ParameterGroupModel> getParameterGroupModels() {
    return parameterGroupModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ExternalLibraryModel> getExternalLibraryModels() {
    return externalLibraryModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StereotypeModel getStereotype() {
    return stereotype;
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
    return "ImmutableConfigurationModel{" +
        "stereotype=" + stereotype +
        ", parameterGroupModels=" + parameterGroupModels +
        ", externalLibraryModels=" + externalLibraryModels +
        ", minMuleVersion=" + minMuleVersion +
        "} " + super.toString();
  }
}
