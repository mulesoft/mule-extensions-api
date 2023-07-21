/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.license;

import org.mule.sdk.api.annotation.MinMuleVersion;

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
@MinMuleVersion("4.1")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresEnterpriseLicense {

  /**
   * @return true if the module can be run using an evaluation license for the runtime, false otherwise.
   */
  boolean allowEvaluationLicense() default false;
}
