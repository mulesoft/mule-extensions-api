/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

/**
 * Base class for a {@link Descriptor} which refers to a parent
 * {@link DeclarationDescriptor}.
 * <p>
 * It also contains a {@link ClassTypeLoader} to be used in the creation
 * of the {@link MetadataType} instances which describes the model's types
 *
 * @since 1.0
 */
abstract class ChildDescriptor implements Descriptor
{

    /**
     * the parent {@link DeclarationDescriptor}
     */
    protected final DeclarationDescriptor declaration;

    /**
     * the {@link ClassTypeLoader} to be used
     */
    protected final ClassTypeLoader typeLoader;

    /**
     * Creates a new instance
     *
     * @param declaration the parent {@link DeclarationDescriptor}
     * @param typeLoader  the {@link ClassTypeLoader} to be used
     */
    ChildDescriptor(DeclarationDescriptor declaration, ClassTypeLoader typeLoader)
    {
        this.declaration = declaration;
        this.typeLoader = typeLoader;
    }
}
