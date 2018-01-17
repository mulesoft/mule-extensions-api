/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import org.mule.api.annotation.NoImplement;

import java.util.Optional;

/**
 * A parameter type that gives the capability of:
 *
 * <ul>
 * <li><b>Get the configured expression:</b> With {@link #getExpression()} returns the possible
 * expression used to resolve the parameter value
 * </li>
 * <li><b>Differ the resolution:</b> Using {@link #resolve()} resolves the parameter value of type
 * {@link T}
 * </ul>
 *
 * @param <T> The type of the value to resolve
 * @since 1.0
 */
@NoImplement
public interface ParameterResolver<T> {

  /**
   * @return Resolves the value of type {@link T} for the given parameter.
   */
  T resolve();

  /**
   * @return The optional expression that generates the required value. May be empty if the value
   * was not generated from an expression
   */
  Optional<String> getExpression();
}
