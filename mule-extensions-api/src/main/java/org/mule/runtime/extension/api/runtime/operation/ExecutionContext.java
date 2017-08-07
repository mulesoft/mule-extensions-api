/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.runtime.config.ConfigurationInstance;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Provides context information about the execution of a modeled component
 *
 * @param <M> the generic type of of the model which represents the component beign executed
 * @since 1.0
 */
public interface ExecutionContext<M extends ComponentModel> {

  /**
   * Returns whether parameter of name {@code parameterName} has a value associated to it.
   *
   * @param parameterName the name of a {@link ParameterModel} of the {@link ComponentModel} being executed
   * @return {@code true} if the parameter is present.
   */
  boolean hasParameter(String parameterName);

  /**
   * Returns the value associated to a parameter of name {@code parameterName}
   *
   * @param parameterName the name of a {@link ParameterModel} of the {@link ComponentModel} being executed
   * @param <T>           the returned value's generic type
   * @return the parameter's value or {@code null}. Notice that {@code null} means that the parameter has been
   * resolved to that value.
   * @throws NoSuchElementException if the parameter is not present.
   */
  <T> T getParameter(String parameterName);

  /**
   * Returns the {@link ConfigurationInstance} for the operation being executed.
   *
   * @return a {@link ConfigurationInstance} consistent with a corresponding {@link ConfigurationModel}
   */
  Optional<ConfigurationInstance> getConfiguration();

  /**
   * @return the {@link ExtensionModel} which owns the {@link #getComponentModel()}
   */
  ExtensionModel getExtensionModel();

  /**
   * @return the model for the component being executed
   */
  M getComponentModel();
}
