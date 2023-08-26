/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the particular place of a {@link Parameter} field in the extension configuration window.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Placement {

  /**
   * Parameter's default order. Indicates that the order is not specified by the extension developer.
   */
  int DEFAULT_ORDER = LayoutModel.DEFAULT_ORDER;

  /**
   * Tab name for parameters that are considered for advanced usage.
   */
  String ADVANCED_TAB = "Advanced";
  /**
   * Tab name for parameters that are considered to be part of a connection configuration.
   */
  String CONNECTION_TAB = "Connection";

  String SECURITY_TAB = "TLS";

  String DEFAULT_TAB = "General";

  String ERROR_MAPPING_TAB = "Error Mapping";

  /**
   * Gives the annotated element a relative order within its group. The value provided may be repeated and in that case the order
   * is not guaranteed.
   * <p>
   * The value is relative meaning that the element with order 10 is on top than one with value 25.
   */
  int order() default DEFAULT_ORDER;

  /**
   * A tab is a logical way to groups together. This attributes specifies the name of the tab in which the annotated element
   * should be displayed. If no tab is specified then a default tab is assumed.
   * <p>
   * To display more than one parameter or field in the same the tab then this value should be exactly the same for all of them.
   * <p>
   * By default the value is {@link #DEFAULT_TAB}
   */
  String tab() default DEFAULT_TAB;
}
