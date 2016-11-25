/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import static java.util.Optional.empty;

import java.util.Optional;

/**
 * {@link ErrorTypeDefinition} {@link Enum} which publish the available errors that the Mule Runtime provides
 * to be extended from an extension.
 *
 * @since 1.0
 */
public enum MuleErrors implements ErrorTypeDefinition<MuleErrors> {
  CONNECTIVITY, TRANSFORMATION, EXPRESSION, REDELIVERY_EXHAUSTED, RETRY_EXHAUSTED, ROUTING, SECURITY, ANY {

    @Override
    public Optional<ErrorTypeDefinition<?>> getParent() {
      return empty();
    }
  };

  @Override
  public Optional<ErrorTypeDefinition<?>> getParent() {
    return Optional.of(ANY);
  }
}
