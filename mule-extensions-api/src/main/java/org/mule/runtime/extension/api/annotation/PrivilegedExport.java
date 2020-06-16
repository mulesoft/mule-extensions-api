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
 * Specifies packages that an extension should expose beyond the default {@link ClassLoader} isolation.
 * <p>
 * This annotation's usage is optional. This annotation's purpose is to allow access to classes that are not part of the public
 * extension's API in border cases. Using this annotation should not be something usual. When needed, this annotation should be
 * placed on the same class that is annotated with {@link Extension}
 * <p>
 * The referenced packages will be visible only by the listed artifacts.<b>USE WITH CARE</b>, negligent use of this annotation
 * could result in class path issues when exported classes conflict with those in the runtime or other extensions consuming the
 * referenced packages.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.PrivilegedExport} instead.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface PrivilegedExport {

  /**
   * @return The additional Java packages that should be exported as privileged API
   */
  String[] packages() default {};

  /**
   * @return The artifacts that have access to the privileged API. Each artifact is defined using Maven's groupId:artifactId.
   */
  String[] artifacts() default {};
}
