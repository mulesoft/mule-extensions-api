/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.exception.IllegalModelDefinitionException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Immutable implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public class ImmutableExtensionModel extends AbstractComplexModel implements ExtensionModel
{

    private final String version;
    private final Map<String, ConfigurationModel> configurations;
    private final String vendor;

    /**
     * Creates a new instance with the given state
     *
     * @param name                the extension's name. Cannot be blank
     * @param description         the extension's description
     * @param version             the extension's version
     * @param vendor              the extension's vendor name
     * @param configurationModels a {@link List} with the extension's {@link ConfigurationModel configurationModels}
     * @param operationModels     a {@link List} with the extension's {@link OperationModel operationModels}
     * @param connectionProviders a {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
     * @param sourceModels        a {@link List} with the extension's {@link SourceModel message source models}
     * @param modelProperties     A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code configurations} or {@link ParameterModel} are {@code null} or contain instances with non unique names, or if {@code name} is blank
     */
    public ImmutableExtensionModel(String name,
                                   String description,
                                   String version,
                                   String vendor,
                                   List<ConfigurationModel> configurationModels,
                                   List<OperationModel> operationModels,
                                   List<ConnectionProviderModel> connectionProviders,
                                   List<SourceModel> sourceModels,
                                   Set<ModelProperty> modelProperties)
    {
        super(name, description, operationModels, connectionProviders, sourceModels, modelProperties);
        this.configurations = toMap(configurationModels);

        checkArgument(version != null && version.length() > 0, "Version cannot be blank");

        if (vendor == null)
        {
            throw new IllegalModelDefinitionException("Vendor cannot be null");
        }

        this.version = version;
        this.vendor = vendor;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConfigurationModel> getConfigurationModels()
    {
        return toList(configurations.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConfigurationModel> getConfigurationModel(String name)
    {
        return findModel(configurations, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVendor()
    {
        return vendor;
    }
}
