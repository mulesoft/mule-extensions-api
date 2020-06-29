/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.deprecated;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicated that the current annotated member is deprecated from the extension.
 * 
 * This annotation can be applied to: Operations, Parameters, Sources, Scopes, Routers, Functions, Configuration, Connection
 * Providers and Extensions.
 *
 * @since 1.2
 * @deprecated use {@link org.mule.sdk.api.annotation.deprecated.Deprecated} instead
 */
@Target({TYPE, FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@java.lang.Deprecated
public @interface Deprecated {


  /**
   * @return a {@link String} that describes why something was deprecated, what can be used as substitute, or both.
   */
  String message();

  /**
   * @return a {@link String} which is the version of the extension in which the annotated member was deprecated.
   */
  String since();

  /**
   * @return a {@link String} which is the version of the extension in which the annotated member will be removed or is removed.
   */
  String toRemoveIn() default "";

}
