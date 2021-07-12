/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks that all the {@link Optional} {@link Parameter}s contained in the annotated class have an exclusive
 * relationship among each other. The exclusive relation stands for "from all the parameters declared in this class, only one can
 * be present at any time" This annotation doesn't override the optionality of the {@link Parameter}s. The required parameter will
 * remain required and the exclusivity condition imposed by this annotation would not affect them. If the case is given in which
 * one of the {@link Optional} {@link Parameter}s must be present (no matter which one), then
 * {@link ExclusiveOptionals#isOneRequired()} must be set to true.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExclusiveOptionals {

  /**
   * Enforces that one of the parameters must be set at any given time
   */
  boolean isOneRequired() default false;
}
