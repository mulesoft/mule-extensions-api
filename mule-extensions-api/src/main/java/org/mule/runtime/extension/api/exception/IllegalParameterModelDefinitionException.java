/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.exception;


import org.mule.runtime.api.meta.model.parameter.ParameterModel;

/**
 * A specialization of {@link IllegalModelDefinitionException} which marks that a {@link ParameterModel} is invalid
 *
 * @since 1.0
 */
public class IllegalParameterModelDefinitionException extends IllegalModelDefinitionException {

  /**
   * Creates a new instance
   *
   * @param message the detail message
   */
  public IllegalParameterModelDefinitionException(String message) {
    super(message);
  }

  /**
   * Creates a new instance
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalParameterModelDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance
   *
   * @param cause the cause
   */
  public IllegalParameterModelDefinitionException(Throwable cause) {
    super(cause);
  }
}
