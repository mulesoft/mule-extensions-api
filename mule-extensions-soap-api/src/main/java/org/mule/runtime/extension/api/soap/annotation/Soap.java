/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.annotation;

import org.mule.runtime.extension.api.soap.SoapServiceProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on a {@link Class} also annotated with @Extension, to list the {@link SoapServiceProvider}s that the
 * extension exposes.
 * <p>
 * A key difference between traditional extensions and Soap based extensions is that the @Extension annotated class does not serve
 * as a configuration, but it can serve as {@link SoapServiceProvider} by implementing it.
 * <p>
 * {@link Soap} cannot live with the @Configurations or @ConnectionProviders annotations, once the @Extension is marked a Soap
 * one, no other Configs or Providers could be declared.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Soap {

  /**
   * An array of classes which implement the {@link SoapServiceProvider} interface.
   *
   * @return A not empty array
   */
  Class<? extends SoapServiceProvider>[] value() default {};
}
