/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.introspection.ModelProperty;

public final class MetadataKeyIdModelProperty implements ModelProperty
{
    private final int order;

    public MetadataKeyIdModelProperty(int order)
    {
        this.order = order;
    }

    @Override
    public String getName()
    {
        return "MetadataKeyId";
    }

    @Override
    public boolean isExternalizable()
    {
        return true;
    }

    public int getOrder()
    {
        return order;
    }
}
