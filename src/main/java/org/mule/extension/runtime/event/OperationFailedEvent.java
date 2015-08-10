/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime.event;

/**
 * An immutable event that signals than an operation execution failed.
 *
 * @since 1.0
 */
public class OperationFailedEvent
{

    private final Exception exception;

    public OperationFailedEvent(Exception exception)
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
