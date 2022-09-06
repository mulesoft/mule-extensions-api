/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.component.value;

import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_EVICTION_INTERVAL_MILLIS;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_ACTIVE;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_IDLE;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_WAIT;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MIN_EVICTION_MILLIS;
import static org.mule.runtime.extension.api.component.value.PoolingProfileValueDeclarer.ExhaustedAction.WHEN_EXHAUSTED_GROW;
import static org.mule.runtime.extension.api.component.value.PoolingProfileValueDeclarer.InitialisationPolicy.INITIALISE_ONE;

import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.extension.api.component.value.PoolingProfileValueDeclarer;

/**
 * Default implementation of {@link PoolingProfileValueDeclarer}
 *
 * @since 1.5
 */
class DefaultPoolingProfileValueDeclarer implements PoolingProfileValueDeclarer, DefaultValueDeclarer.HasValue {

  private int maxActive = DEFAULT_MAX_POOL_ACTIVE;
  private int maxIdle = DEFAULT_MAX_POOL_IDLE;
  private long maxWait = DEFAULT_MAX_POOL_WAIT;
  private int minEvictionMillis = DEFAULT_MIN_EVICTION_MILLIS;
  private int evictionCheckIntervalMillis = DEFAULT_EVICTION_INTERVAL_MILLIS;

  private InitialisationPolicy initialisationPolicy = INITIALISE_ONE;
  private ExhaustedAction exhaustedAction = WHEN_EXHAUSTED_GROW;

  PoolingProfile toPoolingProfile() {
    PoolingProfile pp = new PoolingProfile(maxActive, maxIdle, maxWait, exhaustedActionAsInt(), initialisationPolicyAsInt());
    pp.setMinEvictionMillis(minEvictionMillis);
    pp.setEvictionCheckIntervalMillis(evictionCheckIntervalMillis);

    return pp;
  }

  private int exhaustedActionAsInt() {
    switch (exhaustedAction) {
      case WHEN_EXHAUSTED_GROW:
        return PoolingProfile.WHEN_EXHAUSTED_GROW;
      case WHEN_EXHAUSTED_FAIL:
        return PoolingProfile.WHEN_EXHAUSTED_FAIL;
      case WHEN_EXHAUSTED_WAIT:
        return PoolingProfile.WHEN_EXHAUSTED_WAIT;
      default:
        throw new IllegalArgumentException("Unsupported ExhaustedAction: " + exhaustedAction);
    }
  }

  private int initialisationPolicyAsInt() {
    switch (initialisationPolicy) {
      case INITIALISE_ONE:
        return PoolingProfile.INITIALISE_ONE;
      case INITIALISE_NONE:
        return PoolingProfile.INITIALISE_NONE;
      case INITIALISE_ALL:
        return PoolingProfile.INITIALISE_ALL;
      default:
        throw new IllegalArgumentException("Unsupported InitialisationPolicy: " + initialisationPolicy);
    }
  }

  @Override
  public PoolingProfileValueDeclarer maxActive(int maxActive) {
    this.maxActive = maxActive;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer maxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer initialisationPolicy(InitialisationPolicy initialisationPolicy) {
    this.initialisationPolicy = initialisationPolicy;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer exhaustedAction(ExhaustedAction exhaustedAction) {
    this.exhaustedAction = exhaustedAction;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer maxWait(long maxWait) {
    this.maxWait = maxWait;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer minEvictionMillis(int evictionMillis) {
    this.minEvictionMillis = evictionMillis;
    return this;
  }

  @Override
  public PoolingProfileValueDeclarer evictionCheckIntervalMillis(int intervalMillis) {
    evictionCheckIntervalMillis = intervalMillis;
    return this;
  }

  @Override
  public Object getValue() {
    return toPoolingProfile();
  }
}
