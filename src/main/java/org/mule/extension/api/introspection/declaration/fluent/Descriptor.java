/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExtensionModel;

/**
 * A descriptor is a flat, intermediate representation
 * of an extension model. It's used to describe an extension
 * though a fluent API without dealing with the specific rules
 * and details of the final {@link ExtensionModel}.
 *
 * @since 1.0
 */
public interface Descriptor
{

    /**
     * Returns the root element of this declaration
     *
     * @return a {@link DeclarationDescriptor}
     */
    DeclarationDescriptor getRootDeclaration();

    /**
     * Adds a config of the given name
     *
     * @param name a non blank name
     * @return a {@link ConfigurationDescriptor} which allows describing the created configuration
     */
    default ConfigurationDescriptor withConfig(String name)
    {
        return getRootDeclaration().withConfig(name);
    }

    /**
     * Adds an operation of the given {@code name}
     *
     * @param name a non blank name
     * @return a {@link OperationDescriptor} which allows describing the created operation
     */
    default OperationDescriptor withOperation(String name)
    {
        return getRootDeclaration().withOperation(name);
    }

    /**
     * Adds a connection provider of the given {@code name}
     *
     * @param name a non blank name
     * @return a {@link ConnectionProviderDescriptor} which allows describing the created provider
     */
    default ConnectionProviderDescriptor withConnectionProvider(String name)
    {
        return getRootDeclaration().withConnectionProvider(name);
    }
}
