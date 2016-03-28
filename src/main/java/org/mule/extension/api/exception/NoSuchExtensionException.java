/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.exception;

import org.mule.extension.api.introspection.ExtensionModel;

/**
 * An {@link Exception} to signal that an {@link ExtensionModel} has been referenced
 * but the runtime has no knowledge of it.
 *
 * @since 1.0
 */
public class NoSuchExtensionException extends RuntimeException
{

    public NoSuchExtensionException()
    {
    }

    public NoSuchExtensionException(String message)
    {
        super(message);
    }

    public NoSuchExtensionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NoSuchExtensionException(Throwable cause)
    {
        super(cause);
    }
}
