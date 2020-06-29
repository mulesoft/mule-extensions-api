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

import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Container for the {@link Repeatable} {@link ConfigReference} annotation
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.ConfigReferences} instead.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface ConfigReferences {

  /**
   * @return all the possible {@link ConfigReference}s declarations for the annotated parameter
   */
  ConfigReference[] value() default {};
}
