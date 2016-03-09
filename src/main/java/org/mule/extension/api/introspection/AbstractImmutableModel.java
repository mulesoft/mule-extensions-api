/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.util.Collections;
import java.util.Map;


/**
 * Base class for immutable implementations of the introspection model
 *
 * @since 1.0
 */
abstract class AbstractImmutableModel implements Described, EnrichableModel
{

    private final String name;
    private final String description;
    private final Map<String, Object> modelProperties;

    /**
     * Creates a new instance
     *
     * @param name            the model's name
     * @param description     the model's description
     * @param modelProperties A {@link Map} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractImmutableModel(String name, String description, Map<String, Object> modelProperties)
    {
        checkArgument(name != null && name.length() > 0, "Name attribute cannot be null or blank");

        this.name = name;
        this.description = description != null ? description : "";
        this.modelProperties = modelProperties != null ? Collections.unmodifiableMap(modelProperties) : Collections.emptyMap();
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
    public <T> T getModelProperty(String key)
    {
        checkArgument(key != null && key.length() > 0, "A model property cannot have a blank key");
        return (T) modelProperties.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String,Object> getModelProperties()
    {
        return modelProperties;
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
        return new StringBuilder().append("AbstractImmutableModel{")
                .append("name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", modelProperties=").append(modelProperties).append('}').toString();
    }

    protected static void checkArgument(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalArgumentException(message);
        }
    }
}
