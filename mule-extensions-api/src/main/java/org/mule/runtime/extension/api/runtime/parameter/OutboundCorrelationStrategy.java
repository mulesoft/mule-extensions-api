/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

/**
 * Lists the possible strategies to determine the correlation id that should be sent on an outbound operation which
 * supports correlation.
 * <p>
 * Operations should invoke the {@link #getOutboundCorrelationId(CorrelationInfo, String)} method to obtain the actual value.
 * If empty, then no correlation id should be sent.
 *
 * @since 1.1
 */
public enum OutboundCorrelationStrategy {

  /**
   * Whether to send a correlation id or not is defined by checking the {@link CorrelationInfo#isOutboundCorrelationEnabled()}
   * method. If that method returns {@code false}, then an {@link Optional#empty()} value is returned. Otherwise, the same
   * behavior as {@link #ALWAYS} will be used
   */
  AUTO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getOutboundCorrelationId(CorrelationInfo correlationInfo, String customCorrelationId) {
      if (correlationInfo.isOutboundCorrelationEnabled()) {
        return ALWAYS.getOutboundCorrelationId(correlationInfo, customCorrelationId);
      }

      return empty();
    }
  },

  /**
   * This strategy always yields a correlation id, regardless of {@link CorrelationInfo#isOutboundCorrelationEnabled()}
   */
  ALWAYS {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getOutboundCorrelationId(CorrelationInfo correlationInfo, String customCorrelationId) {
      return customCorrelationId != null ? of(customCorrelationId) : of(correlationInfo.getCorrelationId());
    }
  },

  /**
   * This strategy always returns {@link Optional#empty()}
   */
  NEVER {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getOutboundCorrelationId(CorrelationInfo correlationInfo, String customCorrelationId) {
      return Optional.empty();
    }
  };

  /**
   * Determines the correlation id that should be sent through an outbound operation (if any).
   * <p>
   * Depending on each strategy implementation, this method might return a {@code customCorrelationId}, the value of
   * {@link CorrelationInfo#getCorrelationId()} or an {@link Optional#empty()} value.
   *
   * @param correlationInfo     the current correlation info
   * @param customCorrelationId a custom correlation id set at the operation level. Use {@code null} if no custom value defined
   * @return an optioanl correlation id value
   */
  public abstract Optional<String> getOutboundCorrelationId(CorrelationInfo correlationInfo, String customCorrelationId);
}
