/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.source;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static org.mule.runtime.extension.api.runtime.source.BackPressureMode.WAIT;

import org.mule.runtime.extension.api.runtime.source.BackPressureMode;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows the customization of a {@link Source} available and default {@link BackPressureMode}.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface BackPressure {

  /**
   * Returns the default mode. This value <b>MUST</b> be contained in {@link #supportedModes()}
   *
   * @return the default {@link BackPressureMode}
   */
  BackPressureMode defaultMode() default WAIT;

  /**
   * The {@link BackPressureMode modes} supported by the annotated modes. Use this option when certain modes do not apply for a
   * given source (e.g.: There's no sense in supporting the {@link BackPressureMode#WAIT} mode in http:listener.
   * <p>
   * If not provided, only the {@link BackPressureMode#WAIT} mode is supported. The {@link #defaultMode()} <b>MUST</b> be
   * contained in this array.
   *
   * @return the supported {@link BackPressureMode modes}
   */
  BackPressureMode[] supportedModes() default {WAIT};

}
