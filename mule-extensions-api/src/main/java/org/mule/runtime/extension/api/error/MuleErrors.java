/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.error;

import static java.util.Optional.ofNullable;

import java.util.Optional;

/**
 * {@link ErrorTypeDefinition} {@link Enum} which publish the available errors that the Mule Runtime provides
 * to be extended from an extension.
 *
 * @since 1.0
 */
public enum MuleErrors implements ErrorTypeDefinition<MuleErrors> {

  ANY, CONNECTIVITY(ANY), TRANSFORMATION(ANY), EXPRESSION(ANY), REDELIVERY_EXHAUSTED(ANY), RETRY_EXHAUSTED(ANY), ROUTING(
      ANY), SECURITY(ANY), OVERLOAD(ANY),

  SOURCE_RESPONSE_PARAMETERS, SOURCE_RESPONSE;

  private ErrorTypeDefinition<MuleErrors> parentError;

  MuleErrors(ErrorTypeDefinition<MuleErrors> parentError) {
    this.parentError = parentError;
  }

  MuleErrors() {}

  @Override
  public Optional<ErrorTypeDefinition<?>> getParent() {
    return ofNullable(parentError);
  }
}
