/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import java.util.LinkedList;
import java.util.List;

public final class ConfigurationDeclaration
{

    private final String name;
    private String description;
    private Class<?> declaringClass;
    private List<ParameterDeclaration> parameters = new LinkedList<>();

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

    public Class<?> getDeclaringClass()
    {
        return declaringClass;
    }

    public void setDeclaringClass(Class<?> declaringClass)
    {
        this.declaringClass = declaringClass;
    }

    public List<ParameterDeclaration> getParameters()
    {
        return parameters;
    }
}
