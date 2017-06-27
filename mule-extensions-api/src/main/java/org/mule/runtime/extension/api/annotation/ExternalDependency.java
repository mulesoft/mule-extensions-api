/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated element has a dependency which is not packaged with the extension but needed at runtime, thus
 * it must be provided by the Mule application using the annotated extension.
 * <p>
 * This annotation is intended to be used in the extension only.
 * <p>
 * This annotation is repeatable, which means that any annotated component can depend on many external dependencies.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Repeatable(ExternalDependencies.class)
public @interface ExternalDependency {

  /**
   * @return The dependency name
   */
  String name();

  /**
   * @return The dependency description
   */
  String description() default "";

  /**
   * @return The dependency group ID
   */
  String groupId() default "";

  /**
   * @return The dependency artifact ID
   */
  String artifactId() default "";

  /**
   * @return The minimum dependency version allowed
   */
  String minVersion() default "";

  /**
   * @return The newer dependency version allowed
   */
  String maxVersion() default "";
}
