/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import java.util.concurrent.TimeUnit;

/**
 * A policy around when should a given instances be expired
 * <p/>
 * Notice that this contract is not directly tied to the instance to be expired itself. It's tied
 * to the concept of idle based expiration.
 *
 * @since 1.0
 */
public interface ExpirationPolicy
{

    /**
     * Determines if an instance should be expired based on the last time it was used.
     *
     * @param lastUsed a scalar time value
     * @param timeUnit a {@link TimeUnit} that qualifies the {@code lastUsed}
     * @return {@code true} if expiration should take place. {@code false} otherwise.
     */
    boolean isExpired(long lastUsed, TimeUnit timeUnit);

    /**
     * Returns a scalar value for the maximum amount of time that an instance should be allowed
     * to be idle
     */
    long getMaxIdleTime();

    /**
     * Returns a {@link TimeUnit} that qualifies the {@link #getMaxIdleTime()} value
     */
    TimeUnit getTimeUnit();
}
