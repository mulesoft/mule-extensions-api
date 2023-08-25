/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.exception;


import org.mule.runtime.api.connection.ConnectionProvider;

/**
 * A specialization of {@link IllegalModelDefinitionException} which marks that a {@link ConnectionProvider} is invalid
 *
 * @since 1.0
 */
public class IllegalConnectionProviderModelDefinitionException extends IllegalModelDefinitionException {

  /**
   * Creates a new instance
   *
   * @param message the detail message
   */
  public IllegalConnectionProviderModelDefinitionException(String message) {
    super(message);
  }

  /**
   * Creates a new instance
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalConnectionProviderModelDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance
   *
   * @param cause the cause
   */
  public IllegalConnectionProviderModelDefinitionException(Throwable cause) {
    super(cause);
  }
}

