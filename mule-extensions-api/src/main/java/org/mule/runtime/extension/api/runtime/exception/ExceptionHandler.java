/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.exception;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;
import org.mule.runtime.extension.api.exception.ModuleException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Allows the developer to provide generic logic to enrich exceptions, either via logging, sending notifications, etc.
 * <p>
 * The developer can return a new exception which replaces the original one or return the one that was thrown by the operation.
 * For example, wrapping the Exception into a ConnectionException, the runtime know that reconnection is needed. Notice that this
 * implies that the method should not fail by any reason.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.runtime.exception.ExceptionHandler} instead.
 */
@Deprecated
public abstract class ExceptionHandler {

  /**
   * This method can return a new enriched exception or the original exception after doing some processing with it. It must not
   * return a null value.
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
    return error.isPresent() ? new ModuleException(error.get(), exception) : exception;
  }

  /**
   * Returns the first cause of {@code throwable} which is an instance of {@code causeType}.
   *
   * If {@code throwable} is an instance of {@code causeType} itself, then {@code throwable} is returned.
   * 
   * @param throwable the exception to introspect
   * @param causeType the cause type
   * @param <T> the generic type of the expected exception
   * @return an optional cause
   */
  protected <T extends Throwable> Optional<T> getCauseOfType(Throwable throwable, Class<T> causeType) {
    return getCauseOfType(throwable, causeType, new HashSet<>());
  }

  private <T extends Throwable> Optional<T> getCauseOfType(Throwable throwable, Class<T> causeType,
                                                           Set<Reference<Throwable>> visitedCauses) {
    if (causeType.isInstance(throwable)) {
      return of((T) throwable);
    }

    Throwable cause = throwable.getCause();
    if (cause != null && visitedCauses.add(new Reference<>(cause))) {
      return getCauseOfType(cause, causeType, visitedCauses);
    }

    return empty();
  }

  private Optional<ErrorTypeDefinition> getRootCauseErrorType(Throwable exception,
                                                              Optional<ErrorTypeDefinition> errorType) {
    if (exception == null) {
      return errorType;
    }

    Optional<ErrorTypeDefinition> error =
        exception instanceof ModuleException ? of(((ModuleException) exception).getType()) : errorType;
    return getRootCauseErrorType(exception.getCause(), error);
  }

  private Optional<ErrorTypeDefinition> getRootCauseErrorType(Throwable exception) {
    return getRootCauseErrorType(exception, Optional.empty());
  }
}
