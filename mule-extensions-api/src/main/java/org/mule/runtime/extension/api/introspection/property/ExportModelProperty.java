/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.ModelProperty;

import java.util.List;

/**
 * A {@link ModelProperty} that lists non default classes and resources
 * that the extension exports
 *
 * @since 1.0
 */
public final class ExportModelProperty implements ModelProperty
{

    private final List<MetadataType> exportedTypes;
    private final List<String> exportedResources;

    /**
     * Creates a new instance
     *
     * @param exportedTypes     the {@link List} of {@link MetadataType types} to be exported
     * @param exportedResources the {@link List} of resources to be exported
     */
    public ExportModelProperty(List<MetadataType> exportedTypes, List<String> exportedResources)
    {
        this.exportedTypes = exportedTypes;
        this.exportedResources = exportedResources;
    }

    /**
     * @return {@code export}
     */
    @Override
    public String getName()
    {
        return "export";
    }

    /**
     * @return {@code false}
     */
    @Override
    public boolean isExternalizable()
    {
        return false;
    }

    public List<MetadataType> getExportedTypes()
    {
        return exportedTypes;
    }

    public List<String> getExportedResources()
    {
        return exportedResources;
    }
}
