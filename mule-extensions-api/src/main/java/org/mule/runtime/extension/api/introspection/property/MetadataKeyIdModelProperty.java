/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;


/**
 * A {@link ModelProperty} for {@link ParameterModel} of Sources and Operations parameters that indicates that
 * the parameter is a {@link MetadataKeyId} or a part of it.
 *
 * @since 1.0
 */
public final class MetadataKeyIdModelProperty implements ModelProperty
{
    private final int order;

    public MetadataKeyIdModelProperty(int order)
    {
        this.order = order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "MetadataKeyId";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExternalizable()
    {
        return true;
    }

    /**
     * The order of this parameter in the {@link MetadataKey}.
     *
     * @return the order of the parameter for a composed {@link MetadataKey}, 0 if is a simple {@link MetadataKey}
     */
    public int getOrder()
    {
        return order;
    }
}
