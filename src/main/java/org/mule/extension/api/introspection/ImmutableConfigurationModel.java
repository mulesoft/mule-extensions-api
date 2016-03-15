/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ImmutableConfigurationModel extends AbstractParameterizedModel implements ConfigurationModel
{

    /**
     * Creates a new instance with the given state
     *
     * @param name            the configuration's name
     * @param description     the configuration's description
     * @param parameterModels a {@link List} with the configuration's {@link ParameterModel parameterModels}
     * @param modelProperties A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank or {@code configurationFactory} is {@code null}
     */
    public ImmutableConfigurationModel(String name,
                                       String description,
                                       List<ParameterModel> parameterModels,
                                       Set<ModelProperty> modelProperties)
    {
        super(name, description, modelProperties, parameterModels);
    }
}
