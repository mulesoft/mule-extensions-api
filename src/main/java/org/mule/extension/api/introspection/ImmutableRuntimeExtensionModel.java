/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Runtime Immutable implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public final class ImmutableRuntimeExtensionModel extends ImmutableExtensionModel implements RuntimeExtensionModel
{

    private transient final Optional<ExceptionEnricherFactory> exceptionEnricherFactory;


    /**
     * Creates a new instance with the given state
     *
     * @param name                     the extension's name. Cannot be blank
     * @param description              the extension's description
     * @param version                  the extension's version
     * @param vendor                   the extension's vendor name
     * @param configurationModels      a {@link List} with the extension's {@link ConfigurationModel configurationModels}
     * @param operationModels          a {@link List} with the extension's {@link OperationModel operationModels}
     * @param connectionProviders      a {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
     * @param sourceModels             a {@link List} with the extension's {@link SourceModel message source models}
     * @param modelProperties          A {@link Set} of custom properties which extend this model
     * @param exceptionEnricherFactory an Optional @{@link ExceptionEnricherFactory} that creates a concrete {@link ExceptionEnricher} instance
     * @throws IllegalArgumentException if {@code configurations} or {@link ParameterModel} are {@code null} or contain instances with non unique names, or if {@code name} is blank
     */
    public ImmutableRuntimeExtensionModel(String name,
                                          String description,
                                          String version,
                                          String vendor,
                                          List<ConfigurationModel> configurationModels,
                                          List<OperationModel> operationModels,
                                          List<ConnectionProviderModel> connectionProviders,
                                          List<SourceModel> sourceModels,
                                          Set<ModelProperty> modelProperties,
                                          Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        super(name, description, version, vendor, configurationModels, operationModels, connectionProviders, sourceModels, modelProperties);
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }


    @Override
    @Transient
    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }
}
