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
 * Base class for immutable implementeation of {@link ParametrizedModel}
 *
 * @since 1.0
 */
abstract class AbstractParameterizedModel extends AbstractImmutableModel implements ParametrizedModel
{

    private final List<ParameterModel> parameterModels;

    /**
     * Creates a new instance
     *
     * @param name            the model's name
     * @param description     the model's description
     * @param modelProperties A {@link Set} of custom properties which extend this model
     * @param parameterModels a {@link List} with the source's {@link ParameterModel parameterModels}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractParameterizedModel(String name, String description, Set<ModelProperty> modelProperties, List<ParameterModel> parameterModels)
    {
        super(name, description, modelProperties);
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
