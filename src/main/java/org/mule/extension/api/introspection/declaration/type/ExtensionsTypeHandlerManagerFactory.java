/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.type;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.java.handler.TypeHandlerManager;
import org.mule.metadata.java.handler.TypeHandlerManagerFactory;

/**
 * An implementation of {@link TypeHandlerManagerFactory} which creates instances of
 * {@link TypeHandlerManager} properly configured to be used under the rules of the
 * Extensions API.
 * <p>
 * The extension's runtime should not use {@link TypeHandlerManager} instances
 * which were not created through implementations of this interface
 *
 * @since 1.0
 */
public final class ExtensionsTypeHandlerManagerFactory implements TypeHandlerManagerFactory
{

    /**
     * Creates a {@link TypeHandlerManager} which uses a {@link ExtensionsFieldHandler}
     *
     * @param typeLoader a {@link ClassTypeLoader}
     * @return a {@Link TypeHandlerManager}
     */
    @Override
    public TypeHandlerManager createTypeHandlerManager(ClassTypeLoader typeLoader)
    {
        return TypeHandlerManager.create(typeLoader, new ExtensionsFieldHandler());
    }
}
