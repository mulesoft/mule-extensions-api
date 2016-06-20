/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.runtime.api.message.MuleMessage;
import org.mule.runtime.extension.api.introspection.parameter.AbstractParameterizedModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.util.List;
import java.util.Set;


/**
 * Base class for immutable implementations of the introspection model
 *
 * @since 1.0
 */
public abstract class AbstractComponentModel extends AbstractParameterizedModel implements ComponentModel
{

    private final OutputModel outputPayload;
    private final OutputModel outputAttributes;

    /**
     * Creates a new instance
     *
     * @param name             the model's name
     * @param description      the model's description
     * @param modelProperties  A {@link Set} of custom properties which extend this model
     * @param parameterModels  a {@link List} with the source's {@link ParameterModel parameterModels}
     * @param outputPayload    an {@link OutputModel} which represents the component's output payload
     * @param outputAttributes an {@link OutputModel} which represents the component's attributes on the output {@link MuleMessage}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractComponentModel(String name, String description, Set<ModelProperty> modelProperties,
                                     List<ParameterModel> parameterModels, OutputModel outputPayload, OutputModel outputAttributes)
    {
        super(name, description, modelProperties, parameterModels);
        this.outputPayload = outputPayload;
        this.outputAttributes = outputAttributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputModel getOutputPayload()
    {
        return outputPayload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputModel getOutputAttributes()
    {
        return outputAttributes;
    }
}
