/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime.event;

/**
 * An immutable signal that signals than an operation execution failed.
 *
 * @since 1.0
 */
public class OperationFailedSignal
{

    private final Exception exception;

    /**
     * Creates a new instance that signals about the given {@code exception}
     *
     * @param exception an {@link Exception} thrown by an operation
     */
    public OperationFailedSignal(Exception exception)
    {
        this.exception = exception;
    }

    /**
     * Returns the thrown {@link Exception}
     */
    public Exception getException()
    {
        return exception;
    }
}
