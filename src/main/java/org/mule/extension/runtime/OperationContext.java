/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.Configuration;
import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.Parameter;
import org.mule.extension.runtime.event.OperationFailedEvent;
import org.mule.extension.runtime.event.OperationSuccessfulEvent;

import java.util.function.Consumer;

/**
 * Provides context information about the execution of an operation
 *
 * @since 1.0
 */
public interface OperationContext
{

    /**
     * The {@link Operation} model for the
     * actual processor being executed
     *
     * @return a {@link Operation}
     */
    Operation getOperation();

    /**
     * Returns the value associated to a parameter of name {@code parameterName}
     *
     * @param parameterName the name of a {@link Parameter} of the {@link Operation} being executed
     * @return the parameter's value or {@code null}. Notice that {@code null} doesn't necessarily
     * mean that the parameter is not present. It might have just been resolved to that value
     */
    Object getParameterValue(String parameterName);

    /**
     * Returns an object which is configuring the operation being executed. The actual type
     * of the instance is unknown, but it's guaranteed to be a realisation of the {@link Configuration}
     * model that was set for the operation
     *
     * @param <C> the generic type of the configuration instance
     * @return a {@code C} consistent with a corresponding {@link Configuration} model
     */
    <C> C getConfigurationInstance();

    /**
     * Registers a {@link Consumer} which takes a {@link OperationSuccessfulEvent}
     * and performs an action if the operation associated with {@code this} instance
     * is executed successfully
     *
     * @param handler a {@link Consumer} which handles a {@link OperationSuccessfulEvent}
     */
    void onOperationSuccessful(Consumer<OperationSuccessfulEvent> handler);

    /**
     * Registers a {@link Consumer} which takes a {@link OperationFailedEvent}
     * and performs an action if the operation associated with {@code this} instance
     * throws a exception
     *
     * @param handler a {@link Consumer} which handles a {@link OperationFailedEvent}
     */
    void onOperationFailed(Consumer<OperationFailedEvent> handler);
}
