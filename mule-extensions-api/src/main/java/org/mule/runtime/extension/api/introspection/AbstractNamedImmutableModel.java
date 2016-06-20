/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Base class for immutable implementations of the introspection model
 *
 * @since 1.0
 */
public abstract class AbstractNamedImmutableModel extends AbstractImmutableModel implements Named
{

    private final String name;

    /**
     * Creates a new instance
     *
     * @param name            the model's name
     * @param description     the model's description
     * @param modelProperties A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code name} is blank
     */
    protected AbstractNamedImmutableModel(String name, String description, Set<ModelProperty> modelProperties)
    {
        super(description, modelProperties);

        checkArgument(name != null && name.length() > 0, "Name attribute cannot be null or blank");
        this.name = name;
    }

    protected static void checkArgument(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalArgumentException(message);
        }
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
            return name.equals(((Named) obj).getName());
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

}
