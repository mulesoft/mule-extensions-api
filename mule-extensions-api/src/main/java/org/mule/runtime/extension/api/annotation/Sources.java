/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on a {@link Class} also annotated with {@link Extension}, to list the {@link Source}s that the extension
 * exposes.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sources {

  /**
   * An array of classes which extend the {@link Source} class.
   *
   * @return A not empty array
   */
  Class<? extends Source>[] value();
}
