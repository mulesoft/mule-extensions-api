/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.runtime;

import org.mule.extension.introspection.Configuration;

/**
 * A component responsible for providing instances which are realizations of a give {@link Configuration}
 * model.
 * <p/>
 * Instances are provided through the {@link #get(OperationContext)} method.
 * When that method is invoked, it's up to each implementation to return a brand
 * new instance or one which has already been returned before.
 *
 * @param <T> the type of configuration instances returned
 * @since 1.0
 */
public interface ConfigurationInstanceProvider<T>
{

    /**
     * Returns a configuration instance to serve the given
     * {@code operationContext}. This method may return an
     * instance already returned in the past or a brand new one.
     *
     * @param operationContext a {@link OperationContext}
     * @return a configuration instance
     */
    T get(OperationContext operationContext);

}
