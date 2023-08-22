/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.security;

/**
 * Bundles the outgoing SOAP message that it's being built with a timestamp that carries the creation.
 *
 * @since 1.0
 */
public final class TimestampSecurityStrategy implements SecurityStrategy {

  /**
   * The time difference between creation and expiry time in seconds. After this time the message is invalid.
   */
  private final long timeToLeaveInSeconds;

  public TimestampSecurityStrategy(long timeToLeaveInSeconds) {
    this.timeToLeaveInSeconds = timeToLeaveInSeconds;
  }

  public TimestampSecurityStrategy() {
    this.timeToLeaveInSeconds = 60;
  }

  public long getTimeToLeaveInSeconds() {
    return timeToLeaveInSeconds;
  }

  @Override
  public void accept(SecurityStrategyVisitor visitor) {
    visitor.visitTimestamp(this);
  }
}
