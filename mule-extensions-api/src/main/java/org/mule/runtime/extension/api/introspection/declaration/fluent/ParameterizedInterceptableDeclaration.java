/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link ParameterizedDeclaration} which adds
 * a {@link List} of {@link ParameterDeclaration}
 *
 * @param <T> the concrete type for {@code this} declaration
 * @since 1.0
 */
public abstract class ParameterizedInterceptableDeclaration<T extends ParameterizedInterceptableDeclaration>
    extends InterceptableDeclaration<T> implements ParameterizedDeclaration {

  private List<ParameterDeclaration> parameters = new LinkedList<>();

  /**
   * {@inheritDoc}
   */
  ParameterizedInterceptableDeclaration(String name) {
    super(name);
  }

  /**
   * @return an unmodifiable {@link List} with the available
   * {@link ParameterDeclaration}s
   */
  @Override
  public List<ParameterDeclaration> getParameters() {
    return Collections.unmodifiableList(parameters);
  }

  /**
   * Adds a {@link ParameterDeclaration}
   *
   * @param parameter a not {@code null} {@link ParameterDeclaration}
   * @return this declaration
   * @throws {@link IllegalArgumentException} if {@code parameter} is {@code null}
   */
  public T addParameter(ParameterDeclaration parameter) {
    if (parameter == null) {
      throw new IllegalArgumentException("Can't add a null parameter");
    }

    parameters.add(parameter);
    return (T) this;
  }
}
