/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.exception;


import org.mule.runtime.api.meta.model.operation.OperationModel;

/**
 * A specialization of {@link IllegalModelDefinitionException} which marks that a {@link OperationModel} is invalid
 *
 * @since 1.0
 */
public class IllegalOperationModelDefinitionException extends IllegalModelDefinitionException {

  /**
   * Creates a new instance
   *
   * @param message the detail message
   */
  public IllegalOperationModelDefinitionException(String message) {
    super(message);
  }

  /**
   * Creates a new instance
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalOperationModelDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance
   *
   * @param cause the cause
   */
  public IllegalOperationModelDefinitionException(Throwable cause) {
    super(cause);
  }
}
