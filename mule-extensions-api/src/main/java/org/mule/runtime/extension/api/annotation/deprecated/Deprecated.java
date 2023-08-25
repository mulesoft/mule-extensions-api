/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.deprecated;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import org.mule.sdk.api.annotation.MinMuleVersion;

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
 */
@MinMuleVersion("4.2")
@Target({TYPE, FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
