/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import org.mule.api.annotation.Experimental;
import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.api.connection.ConnectionProvider;

/**
 * Configures the {@link PoolingProfile} of a {@link ConnectionProvider} capable of pooling connections
 *
 * @since 1.5
 */
@Experimental
@NoImplement
public interface PoolingProfileValueDeclarer {

  /**
   * Sets the max number of active connections
   *
   * @param maxActive max number of connections
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer maxActive(int maxActive);

  /**
   * Sets the max number of idle connections
   *
   * @param maxIdle max number of idle connections
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer maxIdle(int maxIdle);

  /**
   * Sets the {@link InitialisationPolicy} on how the pool is initialised
   *
   * @param initialisationPolicy the initialisation policy
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer initialisationPolicy(InitialisationPolicy initialisationPolicy);

  /**
   * Sets the policy on how to act when the pool is exhausted
   *
   * @param exhaustedAction an {@link ExhaustedAction}
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer exhaustedAction(ExhaustedAction exhaustedAction);

  /**
   * Sets the max number of millis to wait when a connection is requested but none is available
   *
   * @param maxWait max wait time in millis
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer maxWait(long maxWait);

  /**
   * Sets the minimum time in millis after which an idle connection becomes eligible for eviction
   *
   * @param evictionMillis eviction time in millis
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer minEvictionMillis(int evictionMillis);

  /**
   * Sets how ofter does the pool checks for idle connections that can be evicted
   *
   * @param intervalMillis eviction interval in millis
   * @return {@code this} instance
   */
  PoolingProfileValueDeclarer evictionCheckIntervalMillis(int intervalMillis);

  /**
   * List the policies on how can a pool be initialised
   *
   * @since 1.0
   */
  @Experimental
  enum InitialisationPolicy {
    /**
     * Doesn't initialise any connection on startup. Connections are initialised on demand.
     */
    INITIALISE_NONE,

    /**
     * Initialises a single connection. Additional ones are initialised on demand.
     */
    INITIALISE_ONE,

    /**
     * Eagerly initialises all available connections
     */
    INITIALISE_ALL;
  }

  /**
   * Policies on how to act when the pool is exhausted
   *
   * @since 1.0
   */
  @Experimental
  enum ExhaustedAction {

    /**
     * If exhausted, augment the pool size
     */
    WHEN_EXHAUSTED_GROW,

    /**
     * If exhausted, wait for a new connection to become eligible
     */
    WHEN_EXHAUSTED_WAIT,

    /**
     * If exhausted, throw an Exception
     */
    WHEN_EXHAUSTED_FAIL
  }
}

