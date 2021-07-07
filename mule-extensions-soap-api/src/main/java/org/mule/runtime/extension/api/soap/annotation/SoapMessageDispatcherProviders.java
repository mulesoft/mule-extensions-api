/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.soap.MessageDispatcherProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used on the Extension class, to list the {@link MessageDispatcherProvider}s that the extension handles.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface SoapMessageDispatcherProviders {

  /**
   * @return an array of {@link MessageDispatcherProvider} implementations.
   */
  Class<? extends MessageDispatcherProvider>[] value() default {};
}
