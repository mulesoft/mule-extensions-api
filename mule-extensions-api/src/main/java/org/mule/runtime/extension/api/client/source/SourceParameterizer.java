/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.source;

import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.client.params.ComponentParameterizer;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;

import java.util.concurrent.TimeUnit;

/**
 * Parameterizes an operation execution performed through the {@link ExtensionsClient}
 *
 * @since 1.6.0
 */
public interface SourceParameterizer extends ComponentParameterizer<SourceParameterizer> {

  SourceParameterizer withBackPressureMode(BackPressureMode backPressureMode);

  SourceParameterizer withFixedSchedulingStrategy(long frequency, TimeUnit timeUnit, long startDelay);

  SourceParameterizer withCronSchedulingStrategy(String expression, String timeZone);

}
