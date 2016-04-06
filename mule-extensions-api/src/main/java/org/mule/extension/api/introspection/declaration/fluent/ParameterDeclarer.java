/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

/**
 * Allows configuring a {@link ParameterDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class ParameterDeclarer<T extends ParameterDeclarer> implements HasModelProperties<ParameterDeclarer<T>>
{

    private final ParameterDeclaration declaration;
    private final ClassTypeLoader typeLoader;

    ParameterDeclarer(ParameterDeclaration declaration, ClassTypeLoader typeLoader)
    {
        this.typeLoader = typeLoader;
        this.declaration = declaration;
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
        declaration.setType(type);
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
        declaration.setDescription(description);
        return (T) this;
    }

    public T withExpressionSupport(ExpressionSupport support)
    {
        declaration.setExpressionSupport(support);
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterDeclarer<T> withModelProperty(ModelProperty modelProperty)
    {
        declaration.addModelProperty(modelProperty);
        return this;
    }

    /**
     * Gets the declaration object for this descriptor
     *
     * @return a {@link ParameterDeclaration}
     */
    public ParameterDeclaration getDeclaration()
    {
        return declaration;
    }
}
