/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.ImmutableSet.of;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.Category;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Immutable implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public class ImmutableExtensionModel extends AbstractComplexModel implements ExtensionModel {

  private final String vendor;
  private final String version;
  private final MuleVersion minMuleVersion;
  private final Category category;
  private final List<ConfigurationModel> configurations;
  private final Set<ObjectType> types;

  /**
   * Creates a new instance with the given state
   *
   * @param name                the extension's name. Cannot be blank
   * @param description         the extension's description
   * @param version             the extension's version
   * @param vendor              the extension's vendor name
   * @param category            the extension's {@link Category}
   * @param minMuleVersion      the extension's minimum {@link MuleVersion}
   * @param configurationModels a {@link List} with the extension's {@link ConfigurationModel configurationModels}
   * @param operationModels     a {@link List} with the extension's {@link OperationModel operationModels}
   * @param connectionProviders a {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
   * @param sourceModels        a {@link List} with the extension's {@link SourceModel message source models}
   * @param types               a {@link Set} with the custom types defined by this extension
   * @param modelProperties     A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code configurations} or {@link ParameterModel} are {@code null} or contain instances with non unique names, or if {@code name} is blank
   */
  public ImmutableExtensionModel(String name,
                                 String description,
                                 String version,
                                 String vendor,
                                 Category category,
                                 MuleVersion minMuleVersion,
                                 List<ConfigurationModel> configurationModels,
                                 List<OperationModel> operationModels,
                                 List<ConnectionProviderModel> connectionProviders,
                                 List<SourceModel> sourceModels,
                                 Set<ObjectType> types,
                                 Set<ModelProperty> modelProperties) {
    super(name, description, operationModels, connectionProviders, sourceModels, modelProperties);
    this.configurations = unique(configurationModels, "Configurations");

    checkModelArgument(version != null && version.length() > 0, "Version cannot be blank");
    checkModelArgument(minMuleVersion != null, "Extension Minimum Mule Version cannot be null");
    checkModelArgument(category != null, "Extension Category cannot be null");
    checkModelArgument(vendor != null, "Extension Vendor cannot be null");

    this.minMuleVersion = minMuleVersion;
    this.category = category;
    this.version = version;
    this.vendor = vendor;
    this.types = types != null ? copyOf(types) : of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ConfigurationModel> getConfigurationModels() {
    return configurations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ConfigurationModel> getConfigurationModel(String name) {
    return findModel(configurations, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVendor() {
    return vendor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Category getCategory() {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MuleVersion getMinMuleVersion() {
    return minMuleVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ObjectType> getTypes() {
    return types;
  }

  private void checkModelArgument(boolean condition, String errorMessage) {
    if (!condition) {
      throw new IllegalModelDefinitionException(errorMessage);
    }
  }
}
