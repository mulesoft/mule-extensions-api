/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.extension.api.introspection.ExtensionModel;

/**
 * Marks that the annotated type is of {@link Extensible} kind,
 * declaring that a given type can be extended by others in the context
 * of the {@link ExtensionModel}.
 *
 * @since 1.0
 */
public class ExtensibleTypeAnnotation implements TypeAnnotation
{

    @Override
    public String getName()
    {
        return "ExtensibleType";
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public int hashCode()
    {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof ExtensibleTypeAnnotation;
    }
}
