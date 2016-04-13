/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

final class ImmutableExtensionManifest implements ExtensionManifest
{

    private final String name;
    private final String description;
    private final String version;
    private final DescriberManifest describerManifest;

    ImmutableExtensionManifest(String name, String description, String version, DescriberManifest describerManifest)
    {
        this.name = name;
        this.description = description;
        this.version = version;
        this.describerManifest = describerManifest;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    @Override
    public DescriberManifest getDescriberManifest()
    {
        return describerManifest;
    }
}
