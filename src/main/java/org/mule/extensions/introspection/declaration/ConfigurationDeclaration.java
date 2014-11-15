/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import org.mule.extensions.introspection.Configuration;
import org.mule.extensions.introspection.ConfigurationInstantiator;

import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link Configuration}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link Configuration}
 *
 * @since 1.0
 */
public final class ConfigurationDeclaration extends CapableDeclaration<ConfigurationDeclaration>
{

    private final String name;
    private String description;
    private List<ParameterDeclaration> parameters = new LinkedList<>();
    private ConfigurationInstantiator configurationInstantiator;

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

    public ConfigurationInstantiator getConfigurationInstantiator()
    {
        return configurationInstantiator;
    }

    public void setConfigurationInstantiator(ConfigurationInstantiator configurationInstantiator)
    {
        this.configurationInstantiator = configurationInstantiator;
    }
}
