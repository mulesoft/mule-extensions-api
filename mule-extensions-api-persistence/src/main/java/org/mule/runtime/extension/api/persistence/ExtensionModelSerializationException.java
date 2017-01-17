/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * {@link RuntimeException} to indicate that a problem occurred serializing or deserializing a {@link ExtensionModel}
 *
 * @since 1.0
 */
class ExtensionModelSerializationException extends RuntimeException {

  ExtensionModelSerializationException(String message, Exception cause) {
    super(message, cause);
  }
}
