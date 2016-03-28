/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ImmutableConfigurationModel extends AbstractComplexModel implements ConfigurationModel
{

    private final List<ParameterModel> parameterModels;

    /**
     * Creates a new instance with the given state
     *
     * @param name                the configuration's name
     * @param description         the configuration's description
     * @param parameterModels     a {@link List} with the configuration's {@link ParameterModel parameterModels}
     * @param operationModels     a {@link List} with the extension's {@link OperationModel operationModels}
     * @param connectionProviders a {@link List} with the extension's {@link ConnectionProviderModel connection provider models}
     * @param sourceModels        a {@link List} with the extension's {@link SourceModel message source models}
     * @param modelProperties     a {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
     */
    public ImmutableConfigurationModel(String name,
                                       String description,
                                       List<ParameterModel> parameterModels,
                                       List<OperationModel> operationModels,
                                       List<ConnectionProviderModel> connectionProviders,
                                       List<SourceModel> sourceModels,
                                       Set<ModelProperty> modelProperties)
    {
        super(name, description, operationModels, connectionProviders, sourceModels, modelProperties);
        this.parameterModels = Collections.unmodifiableList(parameterModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }
}
