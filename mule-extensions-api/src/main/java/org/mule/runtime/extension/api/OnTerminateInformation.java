/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import static java.util.Optional.ofNullable;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;

import java.util.Optional;

public class OnTerminateInformation {

  final SourceCallbackContext callbackContext;
  final Optional<Error> error;



  public OnTerminateInformation(SourceCallbackContext callbackContext, Error error) {
    this.callbackContext = callbackContext;
    this.error = ofNullable(error);
  }

  public SourceCallbackContext getCallbackContext() {
    return callbackContext;
  }

  public Optional<Error> getError() {
    return error;
  }
}
