/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;

/**
 * Immutable implementation of {@link DescriberManifest}
 *
 * @since 1.0
 */
final class ImmutableDescriberManifest implements DescriberManifest
{

    private final String id;
    private final Map<String, String> properties;

    /**
     * Creates a new instance
     *
     * @param id         the describer's ID
     * @param properties the describer's properties
     */
    ImmutableDescriberManifest(String id, Map<String, String> properties)
    {
        this.id = id;
        this.properties = unmodifiableMap(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties()
    {
        return properties;
    }
}
