/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.dsl.xml;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows setting directives regarding syntax and semantics of the generated XML DSL.
 *
 * It can be applied on fields annotated with {@link Parameter} or parameters
 * of methods mapping to operations
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.dsl.xml.ParameterDsl} instead.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface ParameterDsl {

  /**
   * Whether the associated element should support inline definition as child element. Defaults to {@code true}
   */
  boolean allowInlineDefinition() default true;

  /**
   * Whether the associated element should support registry references. Defaults to {@code true}
   */
  boolean allowReferences() default true;

}
