/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Annotation to be used on an {@link Extension} to declare the concrete implementations of a given {@code abstract} {@link Class}
 * or {@code interface}. When a {@link Parameter} or {@link Field} of this base type is found, DSL and tooling support for
 * declaring the sub types implementations will be generated.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SubTypesMapping.class)
public @interface SubTypeMapping {

  /**
   * @return the base {@code abstract} {@link Class} or {@code interface}
   */
  Class<?> baseType();

  /**
   * @return the declared concrete implementations for the given {@code baseType}
   */
  Class<?>[] subTypes();

}
