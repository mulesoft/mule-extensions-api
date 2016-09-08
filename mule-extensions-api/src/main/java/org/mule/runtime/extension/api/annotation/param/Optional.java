/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import org.mule.runtime.extension.api.annotation.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link Parameter} field a optional.
 * All configurable attributes that don't include this annotation are
 * considered required
 *
 * @since 1.0
 */
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Optional {

  /**
   * The default value to use if the
   * user doesn't specify a value
   * for the parameter. It can be
   * the value itself or an expression.
   *
   * Since {@code null} values are not
   * allowed as default values in
   * java annotations, the special
   * {@link #NULL} value was to created
   * to reference that the parameter
   * should default to {@code null}
   *
   * @return the default value
   */
  String defaultValue() default NULL;

  interface DefaultValues {

    /**
     * Denotes that if the parameter is not defined, the value will be taken from the message
     * payload.
     */
    String PAYLOAD = "#[payload]";
  }

  /**
   * Because Java doesn't allow
   * {@code null} values as defaults
   * in annotations, this value is
   * used to represent a {@code null}.
   * This value should only be used
   * by the platform
   */
  String NULL = "THIS IS A SPECIAL NULL VALUE - DO NOT USE";
}
