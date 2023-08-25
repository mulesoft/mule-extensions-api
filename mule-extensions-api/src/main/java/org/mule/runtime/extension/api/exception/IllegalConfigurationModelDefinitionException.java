/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.exception;


import org.mule.runtime.api.meta.model.config.ConfigurationModel;

/**
 * A specialization of {@link IllegalModelDefinitionException} which marks that a {@link ConfigurationModel} is invalid
 *
 * @since 1.0
 */
public class IllegalConfigurationModelDefinitionException extends IllegalModelDefinitionException {

  /**
   * Creates a new instance
   *
   * @param message the detail message
   */
  public IllegalConfigurationModelDefinitionException(String message) {
    super(message);
  }

  /**
   * Creates a new instance
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalConfigurationModelDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance
   *
   * @param cause the cause
   */
  public IllegalConfigurationModelDefinitionException(Throwable cause) {
    super(cause);
  }
}
