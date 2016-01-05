/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.exception;

import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.SourceModel;

/**
 * An {@link Exception} to signal that a {@link SourceModel} has been referenced
 * but the platform has no knowledge of it.
 *
 * @since 1.0
 */
public class NoSuchMessageSourceException extends IllegalArgumentException
{

    public NoSuchMessageSourceException(ExtensionModel extensionModel, String extensionName)
    {
        super(String.format("Message Source '%s' does not exists in extension '%s'", extensionName, extensionModel.getName()));
    }
}
