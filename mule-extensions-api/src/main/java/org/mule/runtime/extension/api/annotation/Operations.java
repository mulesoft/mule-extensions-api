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
 * Allows to reference a list of classes from which an Extension's operations are to be inferred. This annotation is to be used in
 * classes which are also annotated with {@link Extension} and {@link #value()} must reference classes which contain public
 * methods which implement the operation
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operations {

  /**
   * @return An array of classes which contain public methods which implement an operation
   */
  Class<?>[] value();
}
