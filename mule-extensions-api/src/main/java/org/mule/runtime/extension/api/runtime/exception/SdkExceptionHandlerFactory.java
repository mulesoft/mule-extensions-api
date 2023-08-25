/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.exception;

import org.mule.sdk.api.runtime.exception.ExceptionHandler;

/**
 * Creates an instance of an {@link ExceptionHandler}
 *
 * @since 1.5.0
 */
public interface SdkExceptionHandlerFactory {

  /**
   * Creates a new instance of a {@link ExceptionHandler}
   *
   * @return a new {@link ExceptionHandler}
   */
  ExceptionHandler createHandler();
}
