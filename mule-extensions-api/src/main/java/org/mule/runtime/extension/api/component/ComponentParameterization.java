/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component;

import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.extension.internal.component.ComponentParameterizationBuilder;

/**
 * Represents a specific configuration for a concrete {@link ParameterizedModel}.
 * <p>
 * Instances are validated during creation, so no instances must contain invalid parameters according to the {@link #getModel()
 * model}.
 *
 * @param <M> the actual {@link ParameterizedModel} sub-type of the component .
 * 
 * @since 1.5
 */
public interface ComponentParameterization<M extends ParameterizedModel> {

  /**
   * @return the actual model of the declared component.
   */
  M getModel();

  /**
   * Obtains the value of a parameter of the declared component.
   * 
   * @param paramGroupName the name of the parameter group
   * @param paramName      the name of the parameter within the group
   * @return the value of the parameter.
   */
  // TODO W-11214395 determine how complex parameters will be represented.
  Object getParameter(String paramGroupName, String paramName);

  /**
   * Iterates through the parameters and calls the provided {@code action} on each one.
   * 
   * @param action a callback to be called for every parameter.
   */
  void forEachParameter(ParameterAction action);

  /**
   * Callback to be used with {@link ComponentParameterization#forEachParameter(ParameterAction)}.
   */
  // TODO W-11214395 convert this into a visitor with a different method for each param type?
  public interface ParameterAction {

    // TODO W-11214395 determine how complex parameters will be represented.
    void accept(String paramGroupName, String paramName, Object paramValue);
  }

  /**
   * Builder that allows to create new {@link ComponentParameterization} instances.
   *
   * @param <M> the actual {@link ParameterizedModel} sub-type of the component being built.
   */
  public interface Builder<M extends ParameterizedModel> {

    /**
     * Performs validations on the parameters provided through {@link #withParameter(String, String, Object)}. If valid, returns a
     * {@link ComponentParameterization}, otherwise an {@link IllegalStateException} is thrown.
     * 
     * @return a valid {@link ComponentParameterization}.
     * @throws IllegalStateException if any provided parameter is invalid, indicating which parameter and the reason for it being
     *                               invalid.
     */
    ComponentParameterization<M> build() throws IllegalStateException;

    /**
     * Sets a parameter with a given value.
     * 
     * @param paramGroupName the name of the group containing the parameter to set.
     * @param paramName      the name of the parameter within the {@code paramGroupName} group to set.
     * @param paramValue     the value of the parameter to set
     * @return this builder
     * @throws IllegalArgumentException if the provided parameter group and name does not exist for the {@code model} or is not of
     *                                  a valid type.
     */
    Builder<M> withParameter(String paramGroupName, String paramName, Object paramValue) throws IllegalArgumentException;
  }

  /**
   * Provides a brand new builder instance for creating a {@link ComponentParameterization}.
   * 
   * @param <M>   the actual {@link ParameterizedModel} sub-type of the component.
   * @param model the actual model of the declared component.
   * @return a new builder instance.
   */
  public static <M extends ParameterizedModel> Builder<M> builder(M model) {
    return new ComponentParameterizationBuilder<M>()
        .withModel(model);
  }
}
