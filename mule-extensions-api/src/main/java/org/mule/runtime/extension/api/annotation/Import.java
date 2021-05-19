/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

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
