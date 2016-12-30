/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.exception;

import static org.mule.runtime.api.util.Preconditions.checkArgument;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

/**
 * {@link RuntimeException} implementation to throw {@link Exception}s that indicates explicitly
 * the {@link ErrorTypeDefinition} that is wanted to throw.
 * Also gives the possibility to declare to add a message and detailed message for the {@link Error} to built.
 *
 * @since 1.0
 */
public class ModuleException extends RuntimeException {

  private ErrorTypeDefinition type;

  /**
   * @param exception           The {@link ModuleException#getCause()} of this new exception.
   * @param errorTypeDefinition The matched {@link ErrorTypeDefinition},
   * @param <T>                 Type of the {@link ErrorTypeDefinition}
   */
  public <T extends Enum<T>> ModuleException(Exception exception, ErrorTypeDefinition<T> errorTypeDefinition) {
    super(exception);
    checkArgument(errorTypeDefinition != null, "The 'errorTypeDefinition' argument can not be null");
    this.type = errorTypeDefinition;
  }

  /**
   * @param exception           The {@link ModuleException#getCause()} of this new exception.
   * @param errorTypeDefinition The matched {@link ErrorTypeDefinition},
   * @param message             to override the one from the original exception
   * @param <T>                 Type of the {@link ErrorTypeDefinition}
   */
  public <T extends Enum<T>> ModuleException(Exception exception, ErrorTypeDefinition<T> errorTypeDefinition,
                                             String message) {
    super(message, exception);
    checkArgument(errorTypeDefinition != null, "The 'errorTypeDefinition' argument can not be null");
    this.type = errorTypeDefinition;
  }

  /**
   * @param message to override the one from the original exception
   * @param errorTypeDefinition The matched {@link ErrorTypeDefinition},
   * @param <T> Type of the {@link ErrorTypeDefinition}
   */
  protected <T extends Enum<T>> ModuleException(String message, ErrorTypeDefinition<T> errorTypeDefinition) {
    super(message);
    checkArgument(errorTypeDefinition != null, "The 'errorTypeDefinition' argument can not be null");
    this.type = errorTypeDefinition;
  }

  /**
   * @return The {@link ErrorTypeDefinition} of the thrown exception
   */
  public ErrorTypeDefinition getType() {
    return type;
  }
}
