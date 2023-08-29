/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.api.component.Component;

import java.util.concurrent.TimeUnit;

/**
 * A policy around when should a given instances be expired
 * <p/>
 * Notice that this contract is not directly tied to the instance to be expired itself. It's tied to the concept of idle based
 * expiration.
 *
 * @since 1.0
 */
public interface ExpirationPolicy extends Component {

  /**
   * Determines if an instance should be expired based on the last time it was used.
   *
   * @param lastUsed a scalar time value
   * @param timeUnit a {@link TimeUnit} that qualifies the {@code lastUsed}
   * @return {@code true} if expiration should take place. {@code false} otherwise.
   */
  boolean isExpired(long lastUsed, TimeUnit timeUnit);

  /**
   * Returns a scalar value for the maximum amount of time that an instance should be allowed to be idle
   */
  long getMaxIdleTime();

  /**
   * Returns a {@link TimeUnit} that qualifies the {@link #getMaxIdleTime()} value
   */
  TimeUnit getTimeUnit();
}
