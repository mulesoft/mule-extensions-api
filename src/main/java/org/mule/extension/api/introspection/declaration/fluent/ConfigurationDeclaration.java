/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.introspection.ConfigurationModel;

import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link ConfigurationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class ConfigurationDeclaration extends InterceptableDeclaration<ConfigurationDeclaration>
{

    private final String name;
    private String description;
    private List<ParameterDeclaration> parameters = new LinkedList<>();
    private ConfigurationFactory configurationFactory;

    ConfigurationDeclaration(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<ParameterDeclaration> getParameters()
    {
        return parameters;
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
