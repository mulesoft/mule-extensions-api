/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.deprecated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
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
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Deprecated {


  /**
   * @return a {@link String} that describes why something was deprecated, what can be used as substitute, or both.
   */
  String message();

}
