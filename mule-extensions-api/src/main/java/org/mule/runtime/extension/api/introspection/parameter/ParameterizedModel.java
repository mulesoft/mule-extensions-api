/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.parameter;

import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.Named;

import java.util.List;

/**
 * Base interface for a model which contains {@link ParameterModel parameters}
 *
 * @see Described
 * @since 1.0
 */
public interface ParameterizedModel extends Named, Described
{

    /**
     * Returns the {@link ParameterModel parameterModels}
     * available for {@code this} model
     *
     * @return a immutable {@link java.util.List} with {@link ParameterModel}
     * instances. It might be empty but it will never be {@code null}
     */
    List<ParameterModel> getParameterModels();
}
