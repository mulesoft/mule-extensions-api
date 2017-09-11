/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import static java.util.Collections.unmodifiableSet;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.Category;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.function.HasFunctionModels;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public class ImmutableExtensionModel extends AbstractComplexModel implements ExtensionModel, HasFunctionModels {

  private final String vendor;
  private final String version;
  private final Category category;
  private final List<ConfigurationModel> configurations;
  private final List<ConstructModel> constructModels;
  private final List<FunctionModel> functions;
  private final Set<String> privilegedPackages;
  private final Set<String> privilegedArtifacts;
  private final Set<ErrorModel> errors;
  private final Set<ObjectType> types;
  private final Set<String> resources;
  private final XmlDslModel xmlDslModel;
  private final Set<SubTypesModel> subTypes;
  private final Set<ImportedTypeModel> importedTypes;
  private final Set<ExternalLibraryModel> externalLibraries;

  /**
   * Creates a new instance with the given state
   *
   * @param name                  The extension's name. Cannot be blank
   * @param description           The extension's description
   * @param version               The extension's version
   * @param vendor                The extension's vendor name
   * @param category              The extension's {@link Category}
   * @param configurationModels   A {@link List} with the extension's {@link ConfigurationModel configurationModels}
   * @param operationModels       A {@link List} with the extension's {@link OperationModel operationModels}
   * @param connectionProviders   A {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
   * @param sourceModels          A {@link List} with the extension's {@link SourceModel message source models}
   * @param functions             A {@link List} with the extension's {@link FunctionModel function models}
   * @param displayModel          A model which contains directive about how the extension is displayed in the UI
   * @param xmlDslModel           The {@link XmlDslModel} which describes the XML language
   * @param subTypes              A {@link Set} with the sub types defined by this extension
   * @param types                 A {@link Set} with the custom types defined by this extension
   * @param resources             A {@link Set} with the paths to all the resources exposed by this extension
   * @param importedTypes         A {@link Set} of {@link ImportedTypeModel} which describes the types that are imported from other extensions
   * @param errors                A {@link Set} of {@link ErrorModel} which communicates the errors that the current extension handles
   * @param externalLibraryModels a {@link Set} with the extension's {@link ExternalLibraryModel external libraries}
   * @param privilegedPackages a {@link Set} of Java package names to export on the extension's privileged API.
   * @param privilegedArtifacts a {@link Set} of artifact ID that have access to the extension's privileged API.
   * @param modelProperties       A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code configurations} or {@link ParameterModel} are {@code null} or contain instances with non unique names, or if {@code name} is blank.
   */
  public ImmutableExtensionModel(String name,
                                 String description,
                                 String version,
                                 String vendor,
                                 Category category,
                                 List<ConfigurationModel> configurationModels,
                                 List<OperationModel> operationModels,
                                 List<ConnectionProviderModel> connectionProviders,
                                 List<SourceModel> sourceModels,
                                 List<FunctionModel> functions,
                                 List<ConstructModel> constructModels,
                                 DisplayModel displayModel,
                                 XmlDslModel xmlDslModel,
                                 Set<SubTypesModel> subTypes,
                                 Set<ObjectType> types,
                                 Set<String> resources,
                                 Set<ImportedTypeModel> importedTypes,
                                 Set<ErrorModel> errors,
                                 Set<ExternalLibraryModel> externalLibraryModels,
                                 Set<String> privilegedPackages, Set<String> privilegedArtifacts,
                                 Set<ModelProperty> modelProperties) {
    super(name, description, operationModels, connectionProviders, sourceModels, displayModel, modelProperties);
    this.configurations = unique(configurationModels, "Configurations");

    checkModelArgument(version != null && version.length() > 0, "Version cannot be blank");
    checkModelArgument(category != null, "Extension Category cannot be null");
    checkModelArgument(vendor != null, "Extension Vendor cannot be null");
    checkModelArgument(xmlDslModel != null, "xmlDslModel cannot be null");

    this.category = category;
    this.version = version;
    this.vendor = vendor;
    this.types = copy(types);
    this.resources = copy(resources);
    this.importedTypes = copy(importedTypes);
    this.subTypes = copy(subTypes);
    this.xmlDslModel = xmlDslModel;
    this.errors = errors;
    this.externalLibraries = unmodifiableSet(externalLibraryModels);
    this.privilegedPackages = privilegedPackages;
    this.privilegedArtifacts = privilegedArtifacts;
    this.constructModels = unique(constructModels, "Constructs");
    this.functions = unique(functions, "Functions");
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

  @Override
  public List<ConstructModel> getConstructModels() {
    return constructModels;
  }

  @Override
  public Optional<ConstructModel> getConstructModel(String name) {
    return constructModels.stream().filter(c -> c.getName().equals(name)).findFirst();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FunctionModel> getFunctionModels() {
    return functions;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<FunctionModel> getFunctionModel(String name) {
    return findModel(functions, name);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getPrivilegedPackages() {
    return privilegedPackages;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getPrivilegedArtifacts() {
    return privilegedArtifacts;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getResources() {
    return resources;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ImportedTypeModel> getImportedTypes() {
    return importedTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public XmlDslModel getXmlDslModel() {
    return xmlDslModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<SubTypesModel> getSubTypes() {
    return subTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ErrorModel> getErrorModels() {
    return errors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ExternalLibraryModel> getExternalLibraryModels() {
    return externalLibraries;
  }

  private void checkModelArgument(boolean condition, String errorMessage) {
    if (!condition) {
      throw new IllegalModelDefinitionException(errorMessage);
    }
  }
}
