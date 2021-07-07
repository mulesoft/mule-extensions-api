/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.error;

import static java.util.Optional.ofNullable;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;

/**
 * {@link ErrorTypeDefinition} {@link Enum} which publish the available errors that the Mule Runtime provides to be extended from
 * an extension.
 *
 * @since 1.0
 */
@MinMuleVersion("4.0")
public enum MuleErrors implements ErrorTypeDefinition<MuleErrors> {

  /**
   * Wild card that matches with any error and is on top of the error hierarchy for those that allow handling.
   */
  ANY,
  /**
   * Indicates that a problem occurred and a connection could not be established.
   */
  CONNECTIVITY(ANY),
  /**
   * Indicates that a problem occurred when transforming a value.
   */
  TRANSFORMATION(ANY),
  /**
   * Indicates that a problem occurred when resolving an expression.
   */
  EXPRESSION(ANY),
  /**
   * Indicates that the retry policy, of a certain component, to execute some action, eg: connectivity, delivery has been.
   * exhausted
   */
  REDELIVERY_EXHAUSTED(ANY),
  /**
   * Indicates that the retry of a certain execution block has been exhausted.
   */
  RETRY_EXHAUSTED(ANY),
  /**
   * Indicates that a problem occurred when routing a message.
   */
  ROUTING(ANY),
  /**
   * Indicates a security type problem occurred, eg: invalid credentials, expired token, etc.
   */
  SECURITY(ANY),
  /**
   * Indicates a security type problem enforced by an external entity.
   */
  CLIENT_SECURITY(SECURITY),
  /**
   * Indicates a security type problem enforced by the mule runtime.
   */
  SERVER_SECURITY(SECURITY),
  /**
   * Indicates that an error occurred in the source of a flow.
   */
  SOURCE(ANY),
  /**
   * Indicates that an error occurred in the source of the flow processing a successful response.
   */
  SOURCE_RESPONSE(SOURCE),
  /**
   * Indicates that an error occurred in the source of the flow generating the parameters of a successful response.
   */
  SOURCE_RESPONSE_GENERATE(SOURCE_RESPONSE),
  /**
   * Indicates that an error occurred in the source of the flow sending a successful response.
   */
  SOURCE_RESPONSE_SEND(SOURCE_RESPONSE),
  /**
   * Indicates that an error occurred in the source of the flow generating the parameters of an error response.
   */
  SOURCE_ERROR_RESPONSE_GENERATE(SOURCE),
  /**
   * Indicates that an error occurred in the source of the flow sending an error response.
   */
  SOURCE_ERROR_RESPONSE_SEND(SOURCE),
  /**
   * Indicates that a validator failed.
   */
  VALIDATION(ANY),
  /**
   * Indicates that a severe error occurred. Cannot be handled. Top of the error hierarchy for those that do not allow handling.
   */
  CRITICAL;

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
