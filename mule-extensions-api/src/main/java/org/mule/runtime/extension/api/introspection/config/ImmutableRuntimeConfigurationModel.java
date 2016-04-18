/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.config;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.RuntimeExtensionModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;

import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Runtime Immutable implementation of {@link ConfigurationModel}
 *
 * @since 1.0
 */
public final class ImmutableRuntimeConfigurationModel extends ImmutableConfigurationModel implements RuntimeConfigurationModel
{

    private transient final Supplier<RuntimeExtensionModel> extensionModelSupplier;
    private transient final ConfigurationFactory configurationFactory;
    private transient final List<InterceptorFactory> interceptorFactories;

    /**
     * Creates a new instance with the given state
     *
     * @param name                 the configuration's name
     * @param description          the configuration's description
     * @param configurationFactory the {@link ConfigurationFactory}. Cannot be {@code null}
     * @param parameterModels      a {@link List} with the configuration's {@link ParameterModel parameterModels}
     * @param operationModels      a {@link List} with the extension's {@link OperationModel operationModels}
     * @param connectionProviders  a {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
     * @param sourceModels         a {@link List} with the extension's {@link SourceModel message source models}
     * @param modelProperties      a {@link Set} of custom properties which extend this model
     * @param interceptorFactories a {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
     */
    public ImmutableRuntimeConfigurationModel(String name,
                                              String description,
                                              Supplier<RuntimeExtensionModel> extensionModelSupplier,
                                              ConfigurationFactory configurationFactory,
                                              List<ParameterModel> parameterModels,
                                              List<OperationModel> operationModels,
                                              List<ConnectionProviderModel> connectionProviders,
                                              List<SourceModel> sourceModels,
                                              Set<ModelProperty> modelProperties,
                                              List<InterceptorFactory> interceptorFactories)
    {
        super(name, description, parameterModels, operationModels, connectionProviders, sourceModels, modelProperties);
        checkArgument(configurationFactory != null, "Configuration factory cannot be null");

        this.extensionModelSupplier = extensionModelSupplier;
        this.configurationFactory = configurationFactory;
        this.interceptorFactories = interceptorFactories != null ? Collections.unmodifiableList(interceptorFactories) : Collections.emptyList();
    }


    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public RuntimeExtensionModel getExtensionModel()
    {
        return extensionModelSupplier.get();
    }

    @Transient
    @Override
    public List<InterceptorFactory> getInterceptorFactories()
    {
        return interceptorFactories;
    }
}
