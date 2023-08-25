/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.exception;

/**
 * Creates an instance of an {@link ExceptionHandler}
 */
public interface ExceptionHandlerFactory {

  /**
   * Creates a new instance of a {@link ExceptionHandler}
   *
   * @return a new {@link ExceptionHandler}
   */
  ExceptionHandler createHandler();
}
