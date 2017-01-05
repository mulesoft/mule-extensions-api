/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.exception;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;
import org.mule.runtime.extension.api.exception.ModuleException;

import java.util.Optional;

/**
 * Allows the developer to provide generic logic to
 * enrich exceptions, either via logging, sending notifications, etc.
 * <p>
 * The developer can return a new exception which replaces the original one
 * or return the one that was thrown by the operation.
 * For example, wrapping the Exception into a ConnectionException, the runtime know that reconnection is needed.
 * Notice that this implies that the method should not fail by any reason.
 *
 * @since 1.0
 */
public abstract class ExceptionHandler {

  /**
   * This method can return a new enriched exception or the original exception
   * after doing some processing with it. It must not return a null value.
   *
   * Also the implementation of this method needs to be thread safe and must not fail.
   *
   * @param e the exception thrown by the operation
   * @return an Enriched Exception
   *
   * @since 1.0
   */
  public abstract Exception enrichException(Exception e);

  /**
   * This method instrospects the given {@code exception} looking for the deepest {@link ErrorTypeDefinition} in the causes.
   * Returns a a new {@link ModuleException} that wraps the exception received as parameter with the {@link ErrorTypeDefinition}
   * found.
   * 
   * @param exception to introspect
   * @return {@link ModuleException} with the innermost {@link ErrorTypeDefinition} found.
   */
  protected Exception getRootErrorException(Exception exception) {
    Optional<ErrorTypeDefinition> error = getRootCauseErrorType(exception);
    return error.isPresent() ? new ModuleException(exception, error.get()) : exception;
  }

  private Optional<ErrorTypeDefinition> getRootCauseErrorType(Throwable exception,
                                                              Optional<ErrorTypeDefinition> errorType) {
    if (exception == null) {
      return errorType;
    }

    Optional<ErrorTypeDefinition> error =
        exception instanceof ModuleException ? Optional.of(((ModuleException) exception).getType()) : errorType;
    return getRootCauseErrorType(exception.getCause(), error);
  }

  private Optional<ErrorTypeDefinition> getRootCauseErrorType(Throwable exception) {
    return getRootCauseErrorType(exception, Optional.empty());
  }
}
