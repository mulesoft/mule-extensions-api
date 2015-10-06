/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

/**
 * Implementation of {@link ContentMetadata} which allows changing the
 * {@link #outputContentType}
 *
 * @since 1.0
 */
public class MutableContentMetadata implements ContentMetadata
{

    private final ContentType inputContentType;
    protected ContentType outputContentType;

    /**
     * Create a new instance in which both the {@link #inputContentType} and the
     * {@link #outputContentType} are initialised with the {@code inputContentType}
     * argument
     *
     * @param inputContentType the initial {@link ContentType}
     */
    public MutableContentMetadata(ContentType inputContentType)
    {
        this.inputContentType = inputContentType;
        outputContentType = inputContentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentType getInputContentType()
    {
        return inputContentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentType getOutputContentType()
    {
        return outputContentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutputContentType(ContentType outputContentType)
    {
        this.outputContentType = outputContentType;
    }

    /**
     * Always returns {@code true} this this implementation is mutable
     *
     * @return {@code true}
     */
    @Override
    public boolean isOutputModifiable()
    {
        return true;
    }
}
