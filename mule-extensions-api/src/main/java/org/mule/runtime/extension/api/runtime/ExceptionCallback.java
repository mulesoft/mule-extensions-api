/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

/**
 * A callback to notify about exceptions of a given type.
 * <p>
 * Implementations are to be reusable and thread-safe
 *
 * @param <E> the type of the exceptions to be notified
 * @since 1.0
 */
public interface ExceptionCallback<E extends Throwable>
{

    /**
     * Notifies that {@code exception} has occured
     *
     * @param exception a {@link Exception}
     */
    void onException(E exception);
}
