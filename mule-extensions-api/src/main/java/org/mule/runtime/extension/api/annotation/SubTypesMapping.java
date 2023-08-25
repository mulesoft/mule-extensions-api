/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container for the {@link Repeatable} {@link SubTypeMapping} annotation
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SubTypesMapping {

  /**
   * @return all the {@link SubTypeMapping} declarations on the annotated element
   */
  SubTypeMapping[] value() default {};

}
