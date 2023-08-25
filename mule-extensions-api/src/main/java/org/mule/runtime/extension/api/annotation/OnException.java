/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.runtime.exception.ExceptionHandler;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used at Operation level or at {@link Extension} level as a common handler for all the Operations, When
 * an exception occurs in an operation the Enricher Class declared in this annotation will be called immediately, passing to the
 * enrichException method the exception thrown by the operation. an Operation level enricher will override the {@link OnException}
 * level one.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnException {

  /**
   * A {@link Class} which implements the {@link ExceptionHandler} interface.
   */
  Class<? extends ExceptionHandler> value();
}
