/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.runtime;

import org.mule.api.extension.introspection.ConfigurationModel;
import org.mule.api.extension.introspection.OperationModel;
import org.mule.api.extension.introspection.ParameterModel;

/**
 * Provides context information about the execution of an operation
 *
 * @since 1.0
 */
public interface OperationContext
{

    /**
     * Returns the value associated to a parameter of name {@code parameterName}
     *
     * @param parameterName the name of a {@link ParameterModel} of the {@link OperationModel} being executed
     * @return the parameter's value or {@code null}. Notice that {@code null} doesn't necessarily
     * mean that the parameter is not present. It might have just been resolved to that value
     */
    Object getParameter(String parameterName);

    /**
     * Returns the {@link ConfigurationInstance} for the operation being executed.
     *
     * @param <C> the generic type of the configuration instance
     * @return a {@code C} consistent with a corresponding {@link ConfigurationModel}
     */
    <C> ConfigurationInstance<C> getConfiguration();
}
