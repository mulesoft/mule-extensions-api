/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

import org.mule.extension.introspection.ConfigurationModel;
import org.mule.extension.introspection.ConfigurationInstantiator;
import org.mule.extension.runtime.InterceptorFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link ConfigurationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public final class ConfigurationDeclaration extends BaseDeclaration<ConfigurationDeclaration>
{

    private final String name;
    private String description;
    private List<ParameterDeclaration> parameters = new LinkedList<>();
    private ConfigurationInstantiator configurationInstantiator;
    private final List<InterceptorFactory> interceptorFactories = new ArrayList<>();


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

    public ConfigurationDeclaration addInterceptorFactory(InterceptorFactory interceptorFactory)
    {
        interceptorFactories.add(interceptorFactory);
        return this;
    }

    public List<InterceptorFactory> getInterceptorFactories()
    {
        return interceptorFactories;
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
