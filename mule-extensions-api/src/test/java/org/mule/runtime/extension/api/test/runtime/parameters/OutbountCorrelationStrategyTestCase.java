/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.runtime.parameters;

import static org.mule.runtime.extension.api.runtime.parameter.OutboundCorrelationStrategy.ALWAYS;
import static org.mule.runtime.extension.api.runtime.parameter.OutboundCorrelationStrategy.AUTO;
import static org.mule.runtime.extension.api.runtime.parameter.OutboundCorrelationStrategy.NEVER;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.mule.runtime.extension.api.runtime.parameter.CorrelationInfo;

import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class OutbountCorrelationStrategyTestCase {

  private static final String EVENT_ID = "eventId";
  private static final String DEFAULT_CORRELATION_ID = "defaultCorrelationId";
  private static final String CUSTOM_CORRELATION_ID = "customCorrelationId";

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return asList(new Object[][] {
        {"correlation enabled", buildCorrelationInfo(true)},
        {"correlation disabled", buildCorrelationInfo(false)}
    });
  }

  private static CorrelationInfo buildCorrelationInfo(boolean correlationEnabled) {
    CorrelationInfo info = Mockito.mock(CorrelationInfo.class);
    when(info.isOutboundCorrelationEnabled()).thenReturn(correlationEnabled);
    when(info.getEventId()).thenReturn(EVENT_ID);
    when(info.getCorrelationId()).thenReturn(DEFAULT_CORRELATION_ID);

    return info;
  }

  private final CorrelationInfo correlationInfo;

  public OutbountCorrelationStrategyTestCase(String name, CorrelationInfo correlationInfo) {
    this.correlationInfo = correlationInfo;
  }


  @Test
  public void alwaysDefault() {
    assertCorrelation(ALWAYS.getOutboundCorrelationId(correlationInfo, null), DEFAULT_CORRELATION_ID);
  }

  @Test
  public void alwaysCustom() {
    assertCorrelation(ALWAYS.getOutboundCorrelationId(correlationInfo, CUSTOM_CORRELATION_ID), CUSTOM_CORRELATION_ID);
  }

  @Test
  public void autoDefault() {
    final Optional<String> correlationId = AUTO.getOutboundCorrelationId(correlationInfo, null);
    if (correlationInfo.isOutboundCorrelationEnabled()) {
      assertCorrelation(correlationId, DEFAULT_CORRELATION_ID);
    } else {
      assertThat(correlationId.isPresent(), is(false));
    }
  }

  @Test
  public void autoCustom() {
    final Optional<String> correlationId = AUTO.getOutboundCorrelationId(correlationInfo, CUSTOM_CORRELATION_ID);
    if (correlationInfo.isOutboundCorrelationEnabled()) {
      assertCorrelation(correlationId, CUSTOM_CORRELATION_ID);
    } else {
      assertThat(correlationId.isPresent(), is(false));
    }
  }

  @Test
  public void neverDefault() {
    assertThat(NEVER.getOutboundCorrelationId(correlationInfo, null).isPresent(), is(false));
  }

  @Test
  public void neverCustom() {
    assertThat(NEVER.getOutboundCorrelationId(correlationInfo, CUSTOM_CORRELATION_ID).isPresent(), is(false));
  }

  private void assertCorrelation(Optional<String> optional, String expected) {
    String actual = optional.orElse(null);
    assertThat(actual, is(notNullValue()));
    assertThat(actual, is(expected));
  }
}
