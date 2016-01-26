/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

/**
 * Enumerates the different types of support that
 * a given component can provide regarding pooling
 *
 * @since 1.0
 */
public enum PoolingSupport
{
    /**
     * Pooling is supported but not required
     */
    SUPPORTED,
    /**
     * Pooling is required
     */
    REQUIRED,
    /**
     * Pooling is not supported
     */
    NOT_SUPPORTED
}