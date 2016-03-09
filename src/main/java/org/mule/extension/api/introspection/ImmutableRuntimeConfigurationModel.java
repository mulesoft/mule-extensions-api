/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.runtime.InterceptorFactory;

import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
     * @param modelProperties      A {@link Map} of custom properties which extend this model
     * @param interceptorFactories A {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
     */
    public ImmutableRuntimeConfigurationModel(String name,
                                              String description,
                                              Supplier<RuntimeExtensionModel> extensionModelSupplier,
                                              ConfigurationFactory configurationFactory,
                                              List<ParameterModel> parameterModels,
                                              Map<String, Object> modelProperties,
                                              List<InterceptorFactory> interceptorFactories)
    {
        super(name, description, parameterModels, modelProperties);
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

    @Override
    public String toString()
    {
        return new StringBuilder().append("ImmutableRuntimeConfigurationModel{")
                .append(super.toString())
                .append(", extensionModelSupplier=").append(extensionModelSupplier)
                .append(", configurationFactory=").append(configurationFactory)
                .append(", interceptorFactories=").append(interceptorFactories)
                .append('}').toString();
    }
}
