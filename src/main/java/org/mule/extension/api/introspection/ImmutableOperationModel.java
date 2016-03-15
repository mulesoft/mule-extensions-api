/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.metadata.api.model.MetadataType;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class ImmutableOperationModel extends AbstractParameterizedModel implements OperationModel
{

    private final MetadataType returnType;

    /**
     * Creates a new instance with the given state
     *
     * @param name            the operation's name. Cannot be blank
     * @param description     the operation's descriptor
     * @param parameterModels a {@link List} with the operation's {@link ParameterModel parameterModels}
     * @param returnType      a {@link MetadataType} which represents the operation's output
     * @param modelProperties A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
     */
    public ImmutableOperationModel(String name,
                                   String description,
                                   List<ParameterModel> parameterModels,
                                   MetadataType returnType,
                                   Set<ModelProperty> modelProperties)
    {
        super(name, description, modelProperties, parameterModels);

        this.returnType = returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getReturnType()
    {
        return returnType;
    }
}
