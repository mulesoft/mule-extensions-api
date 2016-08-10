/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.connector;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.Extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on a {@link Class} also annotated with {@link Extension}, to list
 * the {@link ConnectionProvider}s that the extension exposes.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Providers {

  /**
   * An array of classes which implement the {@link ConnectionProvider} interface.
   *
   * @return A not empty array
   */
  Class<? extends ConnectionProvider>[] value();
}
