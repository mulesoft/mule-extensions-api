/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an alias for a Java type. Implementations of the extensions API can use this annotation in many ways, but in general
 * terms it's useful to allow a Java type to have a name which makes sense from a coding point of view while maintaining a user
 * friendly name in the places where the extensions API exposes such type
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias {

  /**
   * @return The alias of the annotated member
   */
  String value();

  /**
   * An optional description to further describe the annotated member
   *
   * @return a nullable {@link String}
   */
  String description() default "";
}
