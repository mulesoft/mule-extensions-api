/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection;

import org.mule.extension.runtime.OperationExecutor;

import java.util.List;

/**
 * A definition of an operation in a {@link Extension}
 *
 * @since 1.0
 */
public interface Operation extends Described, Capable
{

    /**
     * Returns the parameters that this operation takes.
     *
     * @return an immutable {@link java.util.List} with instances of
     * {@link Parameter}. It might be
     * empty if the operation takes no parameters, but it will never be {@code null}
     */
    List<Parameter> getParameters();

    /**
     * Provides a {@link OperationExecutor} for this operation.
     *
     * @param configurationInstance an object to act as a concrete configuration instance. This is not a {@link Configuration}
     *                              but the actual object configuring the implementation
     * @param <T>                   the type of the {@code configurationInstance}
     * @return a {@link OperationExecutor}
     */
    <T> OperationExecutor getExecutor(T configurationInstance);
}
