/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;

import java.util.Optional;

/**
 * Immutable {@link ModelProperty} which provides a friendly display name and summary for {@link ExtensionModel},
 * {@link ParameterModel}, {@link SourceModel}, {@link ConfigurationModel} and {@link ConnectionProviderModel}.
 *
 * @since 1.0
 */
public class DisplayModelProperty implements ModelProperty
{

    private String displayName;
    private String summary;

    /**
     * Instantiates a new {@link DisplayModelProperty}
     *
     * @param displayName nullable model display name
     * @param summary     nullable model summary
     */
    public DisplayModelProperty(String displayName, String summary)
    {
        this.displayName = displayName;
        this.summary = summary;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code displayModelProperty}
     */
    @Override
    public String getName()
    {
        return "displayModelProperty";
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

    /**
     * @return the optional display name of the enriched {@link EnrichableModel}
     */
    public Optional<String> getDisplayName()
    {
        return Optional.ofNullable(displayName);
    }

    /**
     * @return the optional summary of the enriched {@link EnrichableModel}
     */
    public Optional<String> getSummary()
    {
        return Optional.ofNullable(summary);
    }
}
