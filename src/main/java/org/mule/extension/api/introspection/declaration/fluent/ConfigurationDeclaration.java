/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.introspection.ConfigurationModel;

/**
 * A declaration object for a {@link ConfigurationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ConfigurationDeclaration extends ParameterizedInterceptableDeclaration<ConfigurationDeclaration>
{

    private ConfigurationFactory configurationFactory;

    /**
     * {@inheritDoc}
     */
    ConfigurationDeclaration(String name)
    {
        super(name);
    }

    public ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    public void setConfigurationFactory(ConfigurationFactory configurationFactory)
    {
        this.configurationFactory = configurationFactory;
    }
}
