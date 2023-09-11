/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field or parameter annotated with {@link Parameter} as actually representing a class.
 * <p/>
 * This annotation should only be used with {@link String} parameters.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Target(value = {PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ClassValue {

  /**
   * @return The Fully Qualified Name of all base classes and interfaces that the referenced class is required to extend or
   *         implement.
   */
  String[] extendsOrImplements() default "";
}
