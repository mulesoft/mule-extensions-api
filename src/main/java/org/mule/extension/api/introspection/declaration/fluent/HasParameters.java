/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.metadata.api.ClassTypeLoader;

/**
 * Base class for an object on which {@link ParameterDeclaration}s can be added
 *
 * @since 1.0
 */
abstract class HasParameters extends ChildDescriptor
{

    public HasParameters(DeclarationDescriptor declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
    }

    /**
     * Adds the {@code parameter}
     *
     * @param parameter a {@link ParameterDeclaration}
     */
    abstract void addParameter(ParameterDeclaration parameter);
}
