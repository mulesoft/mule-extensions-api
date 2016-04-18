/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Base class for immutable implementations of the introspection model
 *
 * @since 1.0
 */
public abstract class AbstractImmutableModel implements Described, EnrichableModel
{

    private final String name;
    private final String description;
    private final Map<Class<? extends ModelProperty>, ModelProperty> modelProperties;

    /**
     * Creates a new instance
     *
     * @param name            the model's name
     * @param description     the model's description
     * @param modelProperties A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractImmutableModel(String name, String description, Set<ModelProperty> modelProperties)
    {
        checkArgument(name != null && name.length() > 0, "Name attribute cannot be null or blank");

        this.name = name;
        this.description = description != null ? description : "";
        this.modelProperties = toModelPropertiesMap(modelProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDescription()
    {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ModelProperty> Optional<T> getModelProperty(Class<T> propertyType)
    {
        checkArgument(propertyType != null, "Cannot get model properties of a null type");
        return Optional.ofNullable((T) modelProperties.get(propertyType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ModelProperty> getModelProperties()
    {
        return Collections.unmodifiableSet(new HashSet(modelProperties.values()));
    }

    /**
     * Defines object equality based on the given object
     * being an object of this class and in the equality
     * of the {@link #getName()} attributes
     *
     * @param obj an object
     * @return {@code true} if equal
     */
    @Override
    public boolean equals(Object obj)
    {
        if (getClass().isInstance(obj))
        {
            return name.equals(((Described) obj).getName());
        }

        return false;
    }

    /**
     * Calculates hashcode based on {@link #getName()}
     *
     * @return a hash code
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    protected static void checkArgument(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalArgumentException(message);
        }
    }

    private Map<Class<? extends ModelProperty>, ModelProperty> toModelPropertiesMap(Collection<ModelProperty> properties)
    {
        if (properties == null || properties.isEmpty())
        {
            return emptyMap();
        }
        return unmodifiableMap(properties.stream().collect(Collectors.toMap(p -> p.getClass(), p -> p)));
    }
}
