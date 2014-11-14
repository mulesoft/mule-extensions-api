/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

public final class ConfigurationConstruct extends HasParameters implements Construct
{

    private final DeclarationConstruct declaration;
    private final ConfigurationDeclaration config;

    ConfigurationConstruct(ConfigurationDeclaration config, DeclarationConstruct declaration)
    {
        this.config = config;
        this.declaration = declaration;
    }

    public WithParameters with()
    {
        return new WithParameters(this, getRootConstruct());
    }

    public ConfigurationConstruct describedAs(String description)
    {
        config.setDescription(description);
        return this;
    }

    @Override
    protected void addParameter(ParameterDeclaration parameter)
    {
        config.getParameters().add(parameter);
    }

    public ConfigurationConstruct declaredIn(Class<?> declaringClass)
    {
        config.setDeclaringClass(declaringClass);
        return this;
    }

    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }
}
