/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime.event;

/**
 * An immutable signal that signals than an operation execution was successful and
 * provides access to its return value.
 *
 * @since 1.0
 */
public class OperationSuccessfulSignal
{

    private final Object result;

    /**
     * Creates a new instance which signals about an operation
     * successfully returning the given {@code result}
     *
     * @param result a result which is the output of an operation execution
     */
    public OperationSuccessfulSignal(Object result)
    {
        this.result = result;
    }

    /**
     * x
     * Returns the operation's result
     */
    public Object getResult()
    {
        return result;
    }
}
