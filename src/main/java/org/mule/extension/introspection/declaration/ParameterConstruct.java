/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.DataType;
import org.mule.extension.introspection.Parameter;

/**
 * A {@link Construct} which allows configuring a {@link ParameterDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class ParameterConstruct<T extends ParameterConstruct> implements Construct, HasCapabilities<ParameterConstruct<T>>    
{

    private final DeclarationConstruct declaration;
    private final ParameterDeclaration parameter;
    private final HasParameters owner;

    ParameterConstruct(HasParameters owner, ParameterDeclaration parameter, DeclarationConstruct declaration)
    {
        this.declaration = declaration;
        this.owner = owner;
        this.parameter = parameter;
    }

    /**
     * Specifies the type of the {@link Parameter} and its parametrized types
     * @param type the type of the parameter
     * @param parametrizedTypes the generic types for {@code type}
     * @return {@value this} construct
     */
    public T ofType(Class<?> type, Class<?>... parametrizedTypes)
    {
        return ofType(DataType.of(type, parametrizedTypes));
    }

    /**
     * Specifies the type of the {@link Parameter}
     * @param dataType the type of the parameter
     * @return
     */
    public T ofType(DataType dataType)
    {
        parameter.setType(dataType);
        return (T) this;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@value this} construct
     */
    public T describedAs(String description)
    {
        parameter.setDescription(description);
        return (T) this;
    }

    /**
     * Specifies that the declared {@link Parameter} is not to be dynamic
     * @return {@value this} construct
     */
    public T whichIsNotDynamic()
    {
        parameter.setDynamic(false);
        return (T) this;
    }

    /**
     * Specifies that the declared {@link Parameter} is to be dynamic
     * (which is the default anyways...)
     * @return {@value this} construct
     */
    public T whichIsDynamic()
    {
        parameter.setDynamic(true);
        return (T) this;
    }

    /**
     * Adds another config of the given name
     * @param name the name of the config
     * @return a new {@link ConfigurationConstruct}
     */
    public ConfigurationConstruct withConfig(String name)
    {
        return getRootConstruct().withConfig(name);
    }

    /**
     * Adds another operation of the given name
     *
     * @param name the name of the config
     * @return a new {@link OperationConstruct}
     */
    public OperationConstruct withOperation(String name)
    {
        return getRootConstruct().withOperation(name);
    }

    /**
     * @return a {@link WithParameters} that allows adding more parameters
     * to the owning {@link Construct}
     */
    public WithParameters with()
    {
        return new WithParameters(owner, getRootConstruct());
    }

    /**
     * @return the root {@link DeclarationConstruct}
     */
    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }

    /**
     * Adds the given capability to the declaring parameter
     *
     * @param capability a not {@code null} capability
     * @return {@value this} construct
     */
    @Override
    public ParameterConstruct<T> withCapability(Object capability)
    {
        parameter.addCapability(capability);
        return this;
    }

    /**
     * Gets the declaration object for this construct
     * @return a {@link ParameterDeclaration}
     */
    public ParameterDeclaration getDeclaration()
    {
        return parameter;
    }
}
