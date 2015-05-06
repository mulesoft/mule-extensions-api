/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.Capable;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Base class for a declaration object on which capabilities can be added
 *
 * @param <T> the type of the capable declaration
 * @since 1.0
 */
public abstract class CapableDeclaration<T extends CapableDeclaration> implements Capable
{

    private Set<Object> capabilities = new HashSet<>();

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
     * @return this construct
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
