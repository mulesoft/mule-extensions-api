/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.ModelProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for a declaration object
 *
 * @param <T> the concrete type for {@code this} declaration
 * @since 1.0
 */
public abstract class BaseDeclaration<T extends BaseDeclaration> implements Described
{

    private final String name;
    private String description = "";
    private final Map<Class<? extends ModelProperty>, ModelProperty> modelProperties = new HashMap<>();

    /**
     * Creates a new instance
     *
     * @param name the name of the component being declared
     */
    public BaseDeclaration(String name)
    {
        this.name = name;
    }

    /**
     * Returns a {@link Set} with the currently added properties. Notice
     * that this {@link Set} is mutable and not thread-safe.
     * <p>
     * This method is to be used when you need to access all the properties.
     * For individual access use {@link #addModelProperty(ModelProperty)}}
     * or {@link #getModelProperty(Class)} instead
     *
     * @return a {@link Set} with the current model properties. Might be empty but will never by {@code null}
     */
    public Set<ModelProperty> getModelProperties()
    {
        return Collections.unmodifiableSet(new HashSet<>(modelProperties.values()));
    }

    /**
     * Returns the model property registered under {@code key}
     *
     * @param propertyType the property's {@link Class}
     * @param <P>          the generic type for the response value
     * @return the associated value wrapped on an {@link Optional}
     */
    public <P extends ModelProperty> Optional<P> getModelProperty(Class<P> propertyType)
    {
        return Optional.ofNullable((P) modelProperties.get(propertyType));
    }

    /**
     * Adds the given {@param modelProperty}. If a property
     * of the same {@link Class} has already been added, it will
     * be overwritten.
     *
     * @param modelProperty a {@link ModelProperty}
     * @throws IllegalArgumentException if {@code modelProperty} is {@code null{}}
     */
    public T addModelProperty(ModelProperty modelProperty)
    {
        if (modelProperty == null)
        {
            throw new IllegalArgumentException("Cannot add a null model property");
        }

        //TODO: MULE-9581 take a look at MetadataKeyBuilder

        modelProperties.put(modelProperty.getClass(), modelProperty);
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the {@link #description} for this declaration
     *
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
