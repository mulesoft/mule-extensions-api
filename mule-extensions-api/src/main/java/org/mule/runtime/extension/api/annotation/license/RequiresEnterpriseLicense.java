/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.license;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the extension requires an enterprise license to run.
 * <p/>
 * Users of this extensions will only be able to run the extension if the runtime has a valid license installed. It's also
 * possible to configure if the extension can be run using an evaluation license by setting {@link #allowEvaluationLicense()} to
 * true.
 *
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresEnterpriseLicense {

  /**
   * @return true if the module can be run using an evaluation license for the runtime, false otherwise.
   */
  boolean allowEvaluationLicense() default false;
}
