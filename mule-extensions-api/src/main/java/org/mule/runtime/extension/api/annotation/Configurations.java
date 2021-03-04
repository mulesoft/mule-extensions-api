/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to reference a list of classes from which a {@code org.mule.runtime.extension.api.introspection.Configuration} model is
 * to be inferred. This annotation is to be used in classes which are also annotated with {@link Extension} and {@link #value()}
 * must reference classes which are annotated with {@link Configuration}
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.Configurations} instead.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configurations {

  /**
   * @return An array of classes annotated with {@link Configuration}
   */
  Class<?>[] value();
}
