/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;

/**
 * A {@link TypeAnnotation} used to enrich a {@link MetadataType} by specifying
 * a certain level of {@link ExpressionSupport} it supports.
 * <p>
 * This class is immutable.
 *
 * @since 1.0
 */
public final class ExpressionSupportAnnotation implements TypeAnnotation
{

    private final ExpressionSupport expressionSupport;

    /**
     * Creates a new instance
     *
     * @param expressionSupport a {@link ExpressionSupport}
     * @throws IllegalArgumentException if {@code expressionSupport} is {@code null}
     */
    public ExpressionSupportAnnotation(ExpressionSupport expressionSupport)
    {
        if (expressionSupport == null)
        {
            throw new IllegalArgumentException("expressionSupport cannot be null");
        }
        this.expressionSupport = expressionSupport;
    }

    public ExpressionSupport getExpressionSupport()
    {
        return expressionSupport;
    }

    @Override
    public String getName()
    {
        return "expressionSupport";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ExpressionSupportAnnotation)
        {
            return expressionSupport == ((ExpressionSupportAnnotation) obj).getExpressionSupport();
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return expressionSupport.hashCode();
    }

    @Override
    public String toString()
    {
        return expressionSupport.name();
    }
}
