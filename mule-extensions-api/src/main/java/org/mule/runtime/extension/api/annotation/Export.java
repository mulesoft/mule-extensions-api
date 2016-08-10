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
 * Specifies classes and resources that an extension should
 * expose beyond the default {@link ClassLoader} isolation.
 * <p>
 * This annotation's usage is optional. By default, the SDK
 * will determine at compile time which are the minimum set
 * of classes and resources that the extension needs to expose
 * in order to function. This annotation's purpose is to allow
 * adding additional artifacts in border cases. Using this annotation
 * should not be something usual. When needed, this annotation
 * should be placed on the same class that is annotated with
 * {@link Extension}
 * <p>
 * The referenced classes and resources will be visible
 * by the runtime and other extensions. <b>USE WITH CARE</b>,
 * negligent use of this annotation could result in class path
 * issues when exported classes conflict with those in the runtime
 * or other extensions consuming the referenced types.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Export {

  /**
   * @return The additional classes that should be exported
   */
  Class[] classes() default {};

  /**
   * @return The additional resources that should be exported
   */
  String[] resources() default {};
}
