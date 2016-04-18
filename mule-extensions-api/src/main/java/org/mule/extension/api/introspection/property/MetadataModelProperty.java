/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.extension.api.exception.IllegalModelDefinitionException;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.parameter.ParameterModel;

/**
 * {@link ModelProperty} for {@link ParameterModel} of Sources and Operations parameters that indicates the involvement
 * of these in the metadata resolution.
 * This model indicate if a parameter is considered as:
 * <ul>
 * <li><b>Content</b>: The parameter is the main input of the operation</li>
 * <li><b>MetadataKeyParam</b>: The parameter is the one that will be represent the {@link MetadataKey}</li>
 * </ul>
 * <p>
 * The only restriction for this Model Property, is that a Parameter can not be a Content and MetadataKeyParam at the
 * same time
 *
 * @since 1.0
 */
public final class MetadataModelProperty implements ModelProperty
{

    private final boolean isMetadataKeyParam;
    private final boolean isContent;

    /**
     * @param isMetadataKeyParam whether or not the parameter is the Metadata Key of the Operation or Source
     * @param isContent          whether or not the parameter is the main input of the Operation
     * @throws IllegalModelDefinitionException if a parameter is declared both as Content and Metadata Key Param
     */
    public MetadataModelProperty(boolean isMetadataKeyParam, boolean isContent)
    {
        if (isContent && isMetadataKeyParam)
        {
            throw new IllegalModelDefinitionException("A parameter cannot be both Content and MetadataKey");
        }

        this.isMetadataKeyParam = isMetadataKeyParam;
        this.isContent = isContent;
    }

    /**
     * @return boolean that indicates if the parameter is a Metadata Key Parameter
     */
    public boolean isMetadataKeyParam()
    {
        return isMetadataKeyParam;
    }

    /**
     * @return boolean indicating if the parameter is the Content of the operation.
     */
    public boolean isContent()
    {
        return isContent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "MetadataProperty";
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true}
     */
    @Override
    public boolean isExternalizable()
    {
        return true;
    }
}
