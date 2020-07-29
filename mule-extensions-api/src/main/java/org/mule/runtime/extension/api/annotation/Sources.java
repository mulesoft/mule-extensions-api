/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on a {@link Class} also annotated with {@link Extension}, to list
 * the {@link Source}s that the extension exposes.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.Sources} instead.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Sources {

  /**
   * An array of classes which extend the {@link Source} class.
   *
   * @return A not empty array
   */
  Class<? extends Source>[] value();
}
