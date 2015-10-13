/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.DataType;
import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.ParameterModel;

/**
 * A {@link Descriptor} which allows configuring a {@link ParameterDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class ParameterDescriptor<T extends ParameterDescriptor> implements Descriptor, HasModelProperties<ParameterDescriptor<T>>
{

    private final DeclarationDescriptor declaration;
    private final ParameterDeclaration parameter;
    private final HasParameters owner;

    ParameterDescriptor(HasParameters owner, ParameterDeclaration parameter, DeclarationDescriptor declaration)
    {
        this.declaration = declaration;
        this.owner = owner;
        this.parameter = parameter;
    }

    /**
     * Specifies the type of the {@link ParameterModel} and its parametrized types
     *
     * @param type              the type of the parameter
     * @param parametrizedTypes the generic types for {@code type}
     * @return {@value this} descriptor
     */
    public T ofType(Class<?> type, Class<?>... parametrizedTypes)
    {
        return ofType(DataType.of(type, parametrizedTypes));
    }

    /**
     * Specifies the type of the {@link ParameterModel}
     *
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
     * @return {@value this} descriptor
     */
    public T describedAs(String description)
    {
        parameter.setDescription(description);
        return (T) this;
    }

    public T withExpressionSupport(ExpressionSupport support)
    {
        parameter.setExpressionSupport(support);
        return (T) this;
    }

    /**
     * Adds another config of the given name
     *
     * @param name the name of the config
     * @return a new {@link ConfigurationDescriptor}
     */
    public ConfigurationDescriptor withConfig(String name)
    {
        return getRootDeclaration().withConfig(name);
    }

    /**
     * Adds another operation of the given name
     *
     * @param name the name of the config
     * @return a new {@link OperationDescriptor}
     */
    public OperationDescriptor withOperation(String name)
    {
        return getRootDeclaration().withOperation(name);
    }

    /**
     * @return a {@link WithParameters} that allows adding more parameters
     * to the owning {@link Descriptor}
     */
    public WithParameters with()
    {
        return new WithParameters(owner, getRootDeclaration());
    }

    /**
     * @return the root {@link DeclarationDescriptor}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return declaration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterDescriptor<T> withModelProperty(String key, Object value)
    {
        parameter.addModelProperty(key, value);
        return this;
    }

    /**
     * Gets the declaration object for this descriptor
     *
     * @return a {@link ParameterDeclaration}
     */
    public ParameterDeclaration getDeclaration()
    {
        return parameter;
    }
}
