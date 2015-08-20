package org.mule.extension.runtime;

import java.util.concurrent.TimeUnit;

/**
 * A policy around when should a given instances be considered elegible for expiration.
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
