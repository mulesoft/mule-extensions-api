/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on an {@link Extension} to declare that a given {@link Class} definition has to be imported from another
 * {@link Extension} declaration.
 *
 * Usages of such {@link Class} will reference its original definition instead of being redefined in the current {@link Extension}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ImportedTypes.class)
public @interface Import {

  /**
   * @return the {@link Class} which definition will be imported
   */
  Class<?> type();
}
