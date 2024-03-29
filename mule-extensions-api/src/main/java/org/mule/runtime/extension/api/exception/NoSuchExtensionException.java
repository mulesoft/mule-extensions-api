/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.exception;

import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * An {@link Exception} to signal that an {@link ExtensionModel} has been referenced but the runtime has no knowledge of it.
 *
 * @since 1.0
 */
public class NoSuchExtensionException extends RuntimeException {

  public NoSuchExtensionException() {}

  public NoSuchExtensionException(String message) {
    super(message);
  }

  public NoSuchExtensionException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchExtensionException(Throwable cause) {
    super(cause);
  }
}
