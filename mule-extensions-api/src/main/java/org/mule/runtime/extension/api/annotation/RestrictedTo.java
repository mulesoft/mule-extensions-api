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
 * Marks that a {@link ElementType#PARAMETER} which references components of an external
 * Extension, should only contain references that are linked to a particular extension.
 * <p/>
 * For example, imagine a method annotated with {@link Operation} which serves as a scope,
 * meaning that the operation contains references to other operations. If you want to limit
 * those nested operations to be only those defined in the Extension A, you can annotate that
 * parameter with {@code @RestrictedTo(A.class)}
 *
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestrictedTo {

  /**
   * A {@link Class} which can unequivocally be used to identify
   * a certain Extension
   */
  Class<?> value();
}
