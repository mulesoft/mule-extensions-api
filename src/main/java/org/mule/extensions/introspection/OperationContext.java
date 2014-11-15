/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;

import java.util.Map;

/**
 * Provides context information about the execution of an operation
 *
 * @since 1.0
 */
public interface OperationContext
{

    /**
     * Returns the actual instance of the configuration to be used
     * to execute the operation. This is <b>NOT</b> an instance of
     * {@link Configuration} but an object which complies with the
     * model described in a {@link Configuration}
     */
    Object getConfigurationInstance();

    /**
     * Returns a {@link Map} in which the keys are the operation's
     * {@link Parameter}s and the values are the actual values to which
     * those parameters are mapped for this particular execution.
     * Any of these values can be {@code null}
     */
    Map<Parameter, Object> getParametersValues();

}
