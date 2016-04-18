/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.metadata;


import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataProperty;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Null {@link MetadataKey} implementation that represents the absence of a key
 *
 * @since 1.0
 */
public final class NullMetadataKey implements MetadataKey
{

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName()
    {
        return "";
    }

    @Override
    public <T extends MetadataProperty> Optional<T> getMetadataProperty(Class<T> propertyType)
    {
        return Optional.empty();
    }

    @Override
    public Set<MetadataProperty> getProperties()
    {
        return Collections.emptySet();
    }
}
