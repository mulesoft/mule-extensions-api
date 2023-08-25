/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.exception;

/**
 * A {@link RuntimeException} thrown when the Extensions API (or a component of any of its implementations) has been used to
 * define an invalid model.
 *
 * @since 1.0
 */
public class IllegalModelDefinitionException extends RuntimeException {

  /**
   * Creates a new instance
   *
   * @param message the detail message
   */
  public IllegalModelDefinitionException(String message) {
    super(message);
  }

  /**
   * Creates a new instance
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalModelDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance
   *
   * @param cause the cause
   */
  public IllegalModelDefinitionException(Throwable cause) {
    super(cause);
  }
}
