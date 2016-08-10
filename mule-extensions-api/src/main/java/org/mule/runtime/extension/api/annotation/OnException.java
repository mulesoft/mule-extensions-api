/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricher;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation can be used at {@link Operation} level or at {@link Extension} level as
 * a common handler for all the {@link Operation}s,
 * When an exception occurs in an operation the Enricher Class declared in this
 * annotation will be called immediately, passing to the enrichException method the exception thrown by the operation.
 * an {@link Operation} level enricher will override the {@link OnException} level one.
 *
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnException {

  /**
   * A {@link Class} which implements the {@link ExceptionEnricher} interface.
   */
  Class<? extends ExceptionEnricher> value();
}
