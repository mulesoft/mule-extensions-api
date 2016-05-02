/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.introspection.ModelProperty;

public final class MetadataContentModelProperty implements ModelProperty
{

    @Override
    public String getName()
    {
        return "MetadataContent";
    }

    @Override
    public boolean isExternalizable()
    {
        return true;
    }
}
