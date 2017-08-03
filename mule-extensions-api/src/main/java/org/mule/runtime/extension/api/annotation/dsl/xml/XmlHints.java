/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.dsl.xml;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
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
 */
@Target({TYPE, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface XmlHints {

  /**
   * Whether the associated element should support inline definition as child element. Defaults to {@code true}
   */
  boolean allowInlineDefinition() default true;

  /**
   * Whether the associated element should support being defined as a top level element.
   * This directive only makes sense when the annotation is applied on a type level, on a
   * class which is being used as a complex parameter. Defaults to {@code false}
   */
  boolean allowTopLevelDefinition() default false;

  /**
   * Whether the associated element should support registry references. Defaults to {@code true}
   */
  boolean allowReferences() default true;

  /**
   * Specifies that the annotated element should extend a particular substitution group.
   * The format expected is prefix:component where the prefix should correspond to an existent one related to an
   * imported namespace. The component should belong to the namespace related to that prefix.
   * If the namespace prefix does not exist or the string doesn't match the required format,
   * an {@code IllegalArgumentException} will be thrown.
   */
  String substitutionGroup() default "";

}
