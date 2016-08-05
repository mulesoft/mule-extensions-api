/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;

/**
 * Used to specify the alias name of the annotated {@link MetadataType}
 *
 * @since 1.0
 */
public class TypeAliasAnnotation implements TypeAnnotation
{

    public static final String NAME = "typeAlias";

    private final String value;

    public TypeAliasAnnotation(String alias)
    {
        this.value = alias;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof TypeAliasAnnotation &&
               ((TypeAliasAnnotation)obj).getValue().equals(this.getValue());
    }
}
