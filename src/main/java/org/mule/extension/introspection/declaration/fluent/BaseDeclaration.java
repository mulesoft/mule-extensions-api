/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

import org.mule.extension.introspection.Capable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class for a declaration object
 *
 * @param <T> the concrete type for {@code this} declaration
 * @since 1.0
 */
public abstract class BaseDeclaration<T extends BaseDeclaration> implements Capable
{

    private Set<Object> capabilities = new HashSet<>();
    private Map<String, Object> modelProperties = new HashMap<>();

    /**
     * @return an unmodifiable copy of the declared capabilities
     * @throws UnsupportedOperationException if mutation of this set is attempted
     */
    public Set<Object> getCapabilities()
    {
        return Collections.unmodifiableSet(capabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Set<C> getCapabilities(Class<C> capabilityType)
    {
        Set<C> matchingCapabilities = new LinkedHashSet<>(capabilities.size());
        for (Object capability : capabilities)
        {
            if (capabilityType.isAssignableFrom(capability.getClass()))
            {
                matchingCapabilities.add((C) capability);
            }
        }

        return Collections.unmodifiableSet(matchingCapabilities);
    }

    /**
     * Adds a capability to this declaration
     *
     * @param capability a not {@code null} capability
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code capability} is {@code null}
     */
    public T addCapability(Object capability)
    {
        if (capability == null)
        {
            throw new IllegalArgumentException("Can't add a null capability");
        }

        capabilities.add(capability);
        return (T) this;
    }

    /**
     * Returns a map with the currently set model properties. Notice
     * that this map is mutable and not thread-safe.
     * <p/>
     * This method is to be used when you need to access all the properties.
     * For individual access use {@link #addModelProperty(String, Object)}
     * or {@link #getModelProperty(String)} instead
     *
     * @return a {@link Map} with the current model properties. Might be empty but will never by {@code null}
     */
    public Map<String, Object> getModelProperties()
    {
        return modelProperties;
    }

    /**
     * Returns the model property registered under {@code key}
     *
     * @param key the property's key
     * @param <T> the generic type for the response value
     * @return the associated value or {@code null} if no such property was registered
     */
    public <T> T getModelProperty(String key)
    {
        return (T) getModelProperties().get(key);
    }

    /**
     * Associates the {@code key} with the {@code value} property
     *
     * @param key   the property's key
     * @param value the property value
     * @throws IllegalArgumentException if key is empty or if {@code value} is {@code null}
     */
    public void addModelProperty(String key, Object value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("A model property's value cannot be null");
        }

        if (key == null || key.trim().length() == 0)
        {
            throw new IllegalArgumentException("A model property's key cannot be null or empty");
        }

        modelProperties.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCapableOf(Class<?> capabilityType)
    {
        for (Object capability : capabilities)
        {
            if (capabilityType.isInstance(capability))
            {
                return true;
            }
        }

        return false;
    }
}
