/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

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
