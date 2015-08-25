/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.exception;

import org.mule.extension.introspection.ExtensionModel;

/**
 * Exception thrown when querying for a specific configuration that is not registered to a given extension.
 *
 * @since 1.0
 */
public final class NoSuchOperationException extends IllegalArgumentException
{

    public NoSuchOperationException(ExtensionModel extensionModel, String extensionName)
    {
        super(String.format("Operation '%s' does not exists in extension '%s'", extensionName, extensionModel.getName()));
    }
}
