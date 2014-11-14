/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;

import java.util.List;

/**
 * A definition of an operation in a {@link Extension}
 *
 * @since 1.0.0
 */
public interface Operation extends Described
{

    /**
     * Returns the parameters that this operation takes.
     *
     * @return an immutable {@link java.util.List} with instances of
     * {@link Parameter}. It might be
     * empty if the operation takes no parameters, but it will never be {@code null}
     */
    List<Parameter> getParameters();

    Class<?> getDeclaringClass();
}
