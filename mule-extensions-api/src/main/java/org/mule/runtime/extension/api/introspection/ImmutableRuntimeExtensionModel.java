/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.Category;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.display.DisplayModel;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricher;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Runtime Immutable implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public final class ImmutableRuntimeExtensionModel extends ImmutableExtensionModel implements RuntimeExtensionModel {

  private transient final Optional<ExceptionEnricherFactory> exceptionEnricherFactory;


  /**
   * Creates a new instance with the given state
   *
   * @param name                     The extension's name. Cannot be blank
   * @param description              The extension's description
   * @param version                  The extension's version
   * @param vendor                   The extension's vendor name
   * @param category                 The extension's {@link Category}
   * @param minMuleVersion           The extension's {@link MuleVersion}
   * @param configurationModels      A {@link List} with the extension's {@link ConfigurationModel configurationModels}
   * @param operationModels          A {@link List} with the extension's {@link OperationModel operationModels}
   * @param connectionProviders      A {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
   * @param sourceModels             A {@link List} with the extension's {@link SourceModel message source models}
   * @param displayModel             A model which contains directive about how this extension is displayed in the UI
   * @param xmlDslModel              The {@link XmlDslModel} which describes the XML language
   * @param subTypes                 A {@link Set} with the sub types defined by this extension
   * @param types                    A {@link Set} with the custom types defined by this extension
   * @param importedTypes            A {@link Set} of {@link ImportedTypeModel} which describes the types that are imported from other extensions
   * @param modelProperties          A {@link Set} of custom properties which extend this model
   * @param exceptionEnricherFactory an Optional @{@link ExceptionEnricherFactory} that creates a concrete {@link ExceptionEnricher} instance
   * @throws IllegalArgumentException if {@code configurations} or {@link ParameterModel} are {@code null} or contain instances with non unique names, or if {@code name} is blank
   */
  public ImmutableRuntimeExtensionModel(String name,
                                        String description,
                                        String version,
                                        String vendor,
                                        Category category,
                                        MuleVersion minMuleVersion,
                                        List<ConfigurationModel> configurationModels,
                                        List<OperationModel> operationModels,
                                        List<ConnectionProviderModel> connectionProviders,
                                        List<SourceModel> sourceModels,
                                        DisplayModel displayModel,
                                        XmlDslModel xmlDslModel,
                                        Set<SubTypesModel> subTypes,
                                        Set<ObjectType> types,
                                        Set<ImportedTypeModel> importedTypes,
                                        Set<ModelProperty> modelProperties,
                                        Optional<ExceptionEnricherFactory> exceptionEnricherFactory) {
    super(name, description, version, vendor, category, minMuleVersion, configurationModels, operationModels, connectionProviders,
          sourceModels, displayModel, xmlDslModel, subTypes, types, importedTypes, modelProperties);
    this.exceptionEnricherFactory = exceptionEnricherFactory;
  }

  @Override
  @Transient
  public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory() {
    return exceptionEnricherFactory;
  }
}
