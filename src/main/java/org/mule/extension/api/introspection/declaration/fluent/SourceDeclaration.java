/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.DataType;
import org.mule.extension.api.introspection.SourceModel;
import org.mule.extension.api.runtime.source.SourceFactory;

/**
 * A declaration object for a {@link SourceModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link SourceModel}
 *
 * @since 1.0
 */
public class SourceDeclaration extends ParameterizedInterceptableDeclaration<SourceDeclaration>
{
    private DataType returnType;
    private DataType attributesType;
    private SourceFactory sourceFactory;

    /**
     * {@inheritDoc}
     */
    SourceDeclaration(String name)
    {
        super(name);
    }

    public DataType getReturnType()
    {
        return returnType;
    }

    public void setReturnType(DataType returnType)
    {
        this.returnType = returnType;
    }

    public SourceFactory getSourceFactory()
    {
        return sourceFactory;
    }

    public void setSourceFactory(SourceFactory sourceFactory)
    {
        this.sourceFactory = sourceFactory;
    }

    public DataType getAttributesType()
    {
        return attributesType;
    }

    public void setAttributesType(DataType attributesType)
    {
        this.attributesType = attributesType;
    }
}
