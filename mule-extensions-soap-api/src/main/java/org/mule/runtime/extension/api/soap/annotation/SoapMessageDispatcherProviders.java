/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
