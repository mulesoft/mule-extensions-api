/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import static java.util.Optional.ofNullable;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.execution.OnTerminate;

import java.util.Optional;

/**
 * A class that represents the result of processing a message from a {@link Source} through a flow,
 * this reports whether the result of the processing was successful or terminated with errors.
 * The purpose of this class is to be used as input of the {@link OnTerminate} callback of a
 * {@link Source} so that this callback can operate according to the result.
 *
 * @since 1.0
 */
public final class SourceResult {

  private final Error parameterGenerationError;
  private final Error responseError;
  private final SourceCallbackContext sourceCallbackContext;

  public static SourceResult success(SourceCallbackContext sourceCallbackContext) {
    return new SourceResult(null, null, sourceCallbackContext);
  }

  public static SourceResult responseError(Error responseError, SourceCallbackContext sourceCallbackContext) {
    return new SourceResult(null, responseError, sourceCallbackContext);
  }

  public static SourceResult parameterError(Error parameterError, SourceCallbackContext sourceCallbackContext) {
    return new SourceResult(parameterError, null, sourceCallbackContext);
  }

  private SourceResult(Error parameterGenerationError, Error responseError, SourceCallbackContext sourceCallbackContext) {
    this.parameterGenerationError = parameterGenerationError;
    this.responseError = responseError;
    this.sourceCallbackContext = sourceCallbackContext;
  }

  /**
   * Indicates whether an error has occurred or not processing a message from a {@link Source} through the owned flow
   *
   * @return boolean indicating the result of the message processing.
   */
  public boolean isSuccess() {
    return this.parameterGenerationError == null &&
        this.responseError == null;
  }

  /**
   * Indicates whether an error occurred or not trying to generate the parameters to call the {@link OnSuccess} or {@link OnError}
   * callbacks.
   *
   * @return boolean indicating if the error happened trying to generate the parameters of the callbacks
   */
  public Optional<Error> getParameterGenerationError() {
    return ofNullable(parameterGenerationError);
  }

  /**
   * Indicates whether an error occurred or not executing one of the source callbacks ({@link OnSuccess} or {@link OnError}).
   *
   * @return boolean indicating if the error happened calling the source callbacks.
   */
  public Optional<Error> getResponseError() {
    return ofNullable(responseError);
  }

  /**
   * @return the {@link SourceCallbackContext} of the message that is being processing.
   */
  public SourceCallbackContext getSourceCallbackContext() {
    return sourceCallbackContext;
  }

}
