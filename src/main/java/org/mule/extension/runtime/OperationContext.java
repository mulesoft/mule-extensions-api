/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.Parameter;

import java.util.Map;

/**
 * Provides context information about the execution of an operation
 *
 * @since 1.0
 */
public interface OperationContext
{

    /**
     * Returns a {@link Map} in which the keys are the operation's
     * {@link Parameter}s and the values are the actual values to which
     * those parameters are mapped for this particular execution.
     * Any of these values can be {@code null}
     */
    Map<Parameter, Object> getParameters();

    /**
     * Returns the value associated to the given {@code parameter}
     *
     * @param parameter a {@link Parameter} of the {@link Operation} being executed
     * @return the parameter's value or {@code null}. Notice that {@code null} doesn't necessarily
     * mean that the parameter is not present. It might have just been resolved to that value
     */
    Object getParameterValue(Parameter parameter);

    /**
     * Returns the value associated to a parameter of name {@code parameterName}
     *
     * @param parameterName the name of a {@link Parameter} of the {@link Operation} being executed
     * @return the parameter's value or {@code null}. Notice that {@code null} doesn't necessarily
     * mean that the parameter is not present. It might have just been resolved to that value
     */
    Object getParameterValue(String parameterName);
}
