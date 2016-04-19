/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import static org.apache.commons.lang3.StringUtils.isBlank;
import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;

/**
 * Immutable implementation of {@link ExtensionManifest}.
 * <p>
 * This class is for internal use only. Users should not reference it.
 *
 * @since 1.0
 */
public final class ImmutableExtensionManifest implements ExtensionManifest
{

    private final String name;
    private final String description;
    private final String version;
    private final DescriberManifest describerManifest;

    /**
     * Creates a new instance
     *
     * @param name              the extension's name
     * @param description       the extension's description
     * @param version           the extension's version
     * @param describerManifest the extension's {@link DescriberManifest}
     */
    public ImmutableExtensionManifest(String name, String description, String version, DescriberManifest describerManifest)
    {
        checkNotBlank(name, "name");
        checkNotBlank(version, "version");

        this.name = name;
        this.description = description;
        this.version = version;
        this.describerManifest = describerManifest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DescriberManifest getDescriberManifest()
    {
        return describerManifest;
    }

    private void checkNotBlank(String value, String attributeName)
    {
        if (isBlank(value))
        {
            throw new IllegalStateException("Manifest cannot have a blank " + attributeName);
        }
    }
}
