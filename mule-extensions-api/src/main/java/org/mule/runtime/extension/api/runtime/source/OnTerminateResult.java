/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.execution.OnTerminate;
import org.mule.runtime.api.message.Error;

import java.util.Optional;

/**
 * Result for the {@link OnTerminate} callbacks. Provides information the result of the Source
 * generated message processing execution.
 *
 * @since 1.0
 */
public final class OnTerminateResult {

  private final Error parameterGenerationError;
  private final Error responseError;
  private final SourceCallbackContext sourceCallbackContext;

  public static OnTerminateResult success(SourceCallbackContext sourceCallbackContext) {
    return new OnTerminateResult(null, null, sourceCallbackContext);
  }

  public static OnTerminateResult responseError(Error responseError, SourceCallbackContext sourceCallbackContext) {
    return new OnTerminateResult(null, responseError, sourceCallbackContext);
  }

  public static OnTerminateResult parameterError(Error parameterError, SourceCallbackContext sourceCallbackContext) {
    return new OnTerminateResult(parameterError, null, sourceCallbackContext);
  }

  private OnTerminateResult(Error parameterGenerationError, Error responseError, SourceCallbackContext sourceCallbackContext) {
    this.parameterGenerationError = parameterGenerationError;
    this.responseError = responseError;
    this.sourceCallbackContext = sourceCallbackContext;
  }

  /**
   * Indicates whether an error occurred or not processing the Source message.
   *
   * @return boolean indicating the result of the message processing.
   */
  public boolean isSuccess() {
    return this.parameterGenerationError == null &&
            this.responseError == null;
  }

  /**
   * Indicates whether an error occurred or not trying to generate the parameters
   * to call the {@link OnSuccess} or {@link OnError} callbacks.
   *
   * @return boolean indicating if the error happened trying to generate the parameters
   * of the callbacks
   */
  public Optional<Error> getParameterGenerationError() {
    return Optional.ofNullable(parameterGenerationError);
  }

  /**
   * Indicates whether an error occurred or not executing one of the source callbacks
   * ({@link OnSuccess} or {@link OnError}).
   *
   * @return boolean indicating if the error happened calling the source callbacks.
   */
  public Optional<Error> getResponseError() {
    return Optional.ofNullable(responseError);
  }

  /**
   * @return the {@link SourceCallbackContext} of the message that is being processing.
   */
  public SourceCallbackContext getSourceCallbackContext() {
    return sourceCallbackContext;
  }

}
