/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.operation.RuntimeOperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.util.NoSuchElementException;

/**
 * Provides context information about the execution of an operation
 *
 * @since 1.0
 */
public interface OperationContext
{

    /**
     * Returns whether parameter of name {@code parameterName} has a value associated to it.
     *
     * @param parameterName the name of a {@link ParameterModel} of the {@link OperationModel} being executed
     * @return {@code true} if the parameter is present.
     */
    boolean hasParameter(String parameterName);

    /**
     * Returns the value associated to a parameter of name {@code parameterName}
     *
     * @param parameterName the name of a {@link ParameterModel} of the {@link OperationModel} being executed
     * @param <T>           the returned value's generic type
     * @return the parameter's value or {@code null}. Notice that {@code null} means that the parameter has been
     *         resolved to that value.
     * @throws NoSuchElementException if the parameter is not present.
     */
    <T> T getParameter(String parameterName);

    /**
     * Same as {@link #getParameter(String)} with the added restriction that the returned value is expected to be either
     * an instance of {@code expectedType} or {@code null}
     *
     * @param parameterName the name of a {@link ParameterModel} of the {@link OperationModel} being executed
     * @param expectedType  a {@link Class} of which the returned value is expected to be an instance of
     * @param <T>           the returned value's expected type
     * @return the parameter's value or {@code null}. Notice that {@code null} means that the parameter has been
     *         resolved to that value.
     * @throws NoSuchElementException if the parameter is not present.
     * @throws IllegalArgumentException if the returned value is not an instance of {@code expectedType}
     */
    <T> T getTypeSafeParameter(String parameterName, Class<? extends T> expectedType);

    /**
     * Returns the {@link ConfigurationInstance} for the operation being executed.
     *
     * @param <C> the generic type of the configuration instance
     * @return a {@link ConfigurationInstance} consistent with a corresponding {@link ConfigurationModel}
     */
    <C> ConfigurationInstance<C> getConfiguration();

    /**
     * Returns the model associated to the operation being executed
     *
     * @return a {@link RuntimeOperationModel}
     */
    RuntimeOperationModel getOperationModel();
}
