/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

/**
 * Allows the developer to provide generic logic to
 * enrich exceptions, either via logging, sending notifications, etc.
 * <p>
 * The developer can return a new exception which replaces the original one
 * or return the one that was thrown by the operation.
 * For example, wrapping the Exception into a ConnectionException, the runtime know that reconnection is needed.
 * Notice that this implies that the method should not fail by any reason.
 *
 * @since 1.0
 */
public interface ExceptionEnricher
{

    /**
     * This method can return a new enriched exception or the original exception
     * after doing some processing with it. It must not return a null value.
     *
     * Also the implementation of this method needs to be thread safe and must not fail.
     *
     * @param e the exception thrown by the operation
     * @return an Enriched Exception
     *
     * @since 1.0
     */
    Exception enrichException(Exception e);
}
