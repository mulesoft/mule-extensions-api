/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

/**
 * Provides metadata regarding the content of a {@code MuleMessage} in the
 * context of an operation execution. As such, it distinguishes between the
 * metadata of the content that enters the operation and the one that goes
 * out of it. Those metadata values are expressed as instances of
 * {@link ContentType}.
 * <p>
 * It also accounts for the fact that in certain cases, it's the operation
 * or even the runtime the one which calculate the output {@link ContentType}.
 * In those cases, the output can be set through the {@link #setOutputContentType(ContentType)}
 * method.
 * <p>
 * There're other cases, in which the operation allows the user to fix the output
 * {@link ContentType}. If the user has explicitly set an output metadata, then
 * {@link #setOutputContentType(ContentType)} must not be allowed to mutate {@code this}
 * instance. That is why the {@link #isOutputModifiable()} should always be checked before
 * trying to mutate this instance.
 * <p>
 * An instance of this class is expected to exist per every operation execution. Therefore,
 * each operation should be provided a mechanism to get a hold of {@code this} instance and
 * mutate conveniently (in the cases in which mutability is allowed). Changes to {code this}
 * instance should be reflected on the underlying {@code MuleMessage}.
 *
 * @see ContentType
 * @since 1.0
 */
public interface ContentMetadata
{

    /**
     * Returns the {@link ContentType} for the content of the {@code MuleMessage} that
     * entered an operation.
     * <p>
     * This value is not mutable in any implementation of this interface
     *
     * @return a {@link ContentType}
     */
    ContentType getInputContentType();

    /**
     * Returns the {@link ContentType} for the content of the {@code MuleMessage} that
     * resulted from executing an operation.
     *
     * @return a {@link ContentType}
     */
    ContentType getOutputContentType();

    /**
     * Sets the output {@link ContentType}.
     * <p>
     * This method will work only as long as {@link #isOutputModifiable()} returns {@code true} on
     * {@code this} instance.
     *
     * @param contentType the value to be set
     * @throws UnsupportedOperationException if invoked on an instance on which {@link #isOutputModifiable()} returns {@code false}
     */
    void setOutputContentType(ContentType contentType);

    /**
     * Indicates if the output {@link ContentType} can be modified.
     * <p>
     * If this method returns {@code true}, then {@link #setOutputContentType(ContentType)} should be guaranteed
     * to succeed. Otherwise, that same method should also be guaranteed to fail.
     *
     * @return whether or not the output metadata can be modifiable.
     */
    boolean isOutputModifiable();
}
