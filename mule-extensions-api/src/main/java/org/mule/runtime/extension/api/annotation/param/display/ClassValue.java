/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field or parameter annotated with {@link Parameter} as actually representing a class.
 * <p/>
 * This annotation should only be used with {@link String} parameters.
 *
 * @since 1.1
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.param.display.ClassValue} instead.
 */
@Target(value = {PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface ClassValue {

  /**
   * @return The Fully Qualified Name of all base classes and interfaces that the referenced class is required to extend
   * or implement.
   */
  String[] extendsOrImplements() default "";
}
