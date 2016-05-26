/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.connection.HasConnectionProviderModels;
import org.mule.runtime.extension.api.introspection.operation.HasOperationModels;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterizedModel;
import org.mule.runtime.extension.api.introspection.source.HasSourceModels;
import org.mule.runtime.extension.api.introspection.source.SourceModel;

/**
 * Convenience base implementation of {@link ExtensionWalker}
 * which provides default implementations for all methods
 * so that the user only has to implement the ones that he cares
 * about.
 *
 * @since 1.0
 */
public abstract class BaseExtensionWalker extends ExtensionWalker
{

    /**
     * No-Op implementation
     * {@inheritDoc}
     */
    @Override
    public void onConfiguration(ConfigurationModel model)
    {

    }

    /**
     * No-Op implementation
     * {@inheritDoc}
     */
    @Override
    public void onOperation(HasOperationModels owner, OperationModel model)
    {

    }

    /**
     * No-Op implementation
     * {@inheritDoc}
     */
    @Override
    public void onConnectionProvider(HasConnectionProviderModels owner, ConnectionProviderModel model)
    {

    }

    /**
     * No-Op implementation
     * {@inheritDoc}
     */
    @Override
    public void onSource(HasSourceModels owner, SourceModel model)
    {

    }

    /**
     * No-Op implementation
     * {@inheritDoc}
     */
    @Override
    public void onParameter(ParameterizedModel owner, ParameterModel model)
    {

    }
}
