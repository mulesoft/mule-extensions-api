/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

/**
 * Implementation of {@link ContentMetadata} which does not allow changing
 * the output metadata. Therefore, this class is immutable.
 *
 * @since 1.0
 */
public final class FixedContextMetadata extends MutableContentMetadata
{

    /**
     * Creates a new instance for the given {@link ContentType} values
     *
     * @param inputContentType  the input {@link ContentType}
     * @param outputContentType the output {@link ContentType}
     */
    public FixedContextMetadata(ContentType inputContentType, ContentType outputContentType)
    {
        super(inputContentType);
        this.outputContentType = outputContentType;
    }

    /**
     * This method is not supported on this implementation since {@link #isOutputModifiable()} is fixed
     * at {@code false}.
     *
     * @param outputContentType a {@link ContentType} which will not be set
     * @throws UnsupportedOperationException on every invokation
     */
    @Override
    public void setOutputContentType(ContentType outputContentType)
    {
        throw new UnsupportedOperationException("The output ContentType has been fixed by the user. Always check isOutputModifiable() before invoking this method");
    }

    /**
     * Always returns {@code false} since this implementation is immutable
     *
     * @return {@code false}
     */
    @Override
    public boolean isOutputModifiable()
    {
        return false;
    }
}
