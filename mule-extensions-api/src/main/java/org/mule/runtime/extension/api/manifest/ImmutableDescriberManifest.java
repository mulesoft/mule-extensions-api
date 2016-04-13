/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;

final class ImmutableDescriberManifest implements DescriberManifest
{

    private final String id;
    private final Map<String, String> properties;

    ImmutableDescriberManifest(String id, Map<String, String> properties)
    {
        this.id = id;
        this.properties = unmodifiableMap(properties);
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return properties;
    }
}
