/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

/**
 * A {@link Descriptor} which allows configuring a {@link ParameterDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class ParameterDescriptor<T extends ParameterDescriptor> extends ChildDescriptor implements HasModelProperties<ParameterDescriptor<T>>
{

    private final ParameterDeclaration parameter;
    private final HasParameters owner;

    ParameterDescriptor(HasParameters owner, ParameterDeclaration parameter, DeclarationDescriptor declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
        this.owner = owner;
        this.parameter = parameter;
    }

    /**
     * Specifies the type of the {@link ParameterModel} and its parametrized types
     *
     * @param type the type of the parameter
     * @return {@code this} descriptor
     */
    public T ofType(Class<?> type)
    {
        return ofType(typeLoader.load(type));
    }

    /**
     * Specifies the type of the {@link ParameterModel}
     *
     * @param type the type of the parameter
     * @return
     */
    public T ofType(MetadataType type)
    {
        parameter.setType(type);
        return (T) this;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} descriptor
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
        return new WithParameters(owner, getRootDeclaration(), typeLoader);
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
