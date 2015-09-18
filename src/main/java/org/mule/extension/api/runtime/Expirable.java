/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

/**
 * An entity which expires based on a {@link ExpirationPolicy}
 *
 * @since 1.0
 */
public interface Expirable
{

    /**
     * Determines if {@code this} instance should be expired based on the given
     * {@code expirationPolicy}
     *
     * @param expirationPolicy a {@link ExpirationPolicy}
     * @return {@code true} if {@code this} can be expired. {@code false} otherwise
     */
    boolean isExpired(ExpirationPolicy expirationPolicy);
}
