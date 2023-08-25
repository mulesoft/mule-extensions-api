/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
