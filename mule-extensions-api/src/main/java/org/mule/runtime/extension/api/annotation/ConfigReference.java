/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used in a {@link String} type {@link Parameter} field or parameter which value is a reference to a global
 * configuration element.
 *
 * @since 1.0
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Repeatable(ElementReferences.class)
public @interface ConfigReference {

  /**
   * @return the name of the extension that contains the accepted configuration type.
   */
  String namespace();

  /**
   * @return the name of the configuration type that is accepted by the annotated {@link Parameter}.
   */
  String name();
}
