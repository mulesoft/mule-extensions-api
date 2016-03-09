/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.metadata.api.model.MetadataType;

import java.util.List;
import java.util.Map;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class ImmutableSourceModel extends AbstractParameterizedModel implements SourceModel
{

    private final MetadataType returnType;
    private final MetadataType attributesType;

    /**
     * Creates a new instance
     *
     * @param name            the source name. Cannot be blank
     * @param description     the source description
     * @param parameterModels a {@link List} with the source's {@link ParameterModel parameterModels}
     * @param returnType      a {@link MetadataType} which represents the payload of generated messages
     * @param attributesType  a {@link MetadataType} which represents the attributes on the generated messages
     * @param modelProperties A {@link Map} of custom properties which extend this model
     */
    public ImmutableSourceModel(String name,
                                String description,
                                List<ParameterModel> parameterModels,
                                MetadataType returnType,
                                MetadataType attributesType,
                                Map<String, Object> modelProperties)
    {
        super(name, description, modelProperties, parameterModels);
        this.returnType = returnType;
        this.attributesType = attributesType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getReturnType()
    {
        return returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getAttributesType()
    {
        return attributesType;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("ImmutableSourceModel{")
                .append(super.toString())
                .append(", returnType=").append(returnType)
                .append(", attributesType=").append(attributesType)
                .append('}').toString();
    }
}
