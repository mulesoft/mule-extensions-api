/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.source;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.client.params.ComponentParameterizer;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.concurrent.TimeUnit;

/**
 * Parameterizes a source created through the {@link ExtensionsClient}
 * <p>
 * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
 *
 * @since 1.5.0
 */
@Experimental
@MinMuleVersion("4.5.0")
public interface SourceParameterizer extends ComponentParameterizer<SourceParameterizer> {

  /**
   * Sets the {@link BackPressureMode} for the source
   *
   * @param backPressureMode the back pressure mode
   * @return {@code this} instance
   */
  SourceParameterizer withBackPressureMode(BackPressureMode backPressureMode);

  /**
   * Specifies a fixed frequency scheduling strategy. Only invoke for polling based sources which specify a scheduling strategy
   * parameter.
   *
   * @param frequency  the executing frequency
   * @param timeUnit   the {@code frequency} {@link TimeUnit}
   * @param startDelay the start delay
   * @return {@code this} instance
   */
  SourceParameterizer withFixedSchedulingStrategy(long frequency, TimeUnit timeUnit, long startDelay);

  /**
   * Specifies a cron expression scheduling strategy. Only invoke for polling based sources which specify a scheduling strategy
   * parameter.
   *
   * @param expression the cron expression
   * @param timeZone   the cron timezone
   * @return {@code this} instance
   */
  SourceParameterizer withCronSchedulingStrategy(String expression, String timeZone);

}
