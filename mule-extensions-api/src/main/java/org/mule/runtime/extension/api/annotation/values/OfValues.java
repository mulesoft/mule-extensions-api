/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.values;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a parameter or parameter group as a value that has the capability of resolve {@link Value values} for this one. This
 * resolution is resolved by the {@link ValueProvider} referenced in the {@link OfValues}.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface OfValues {

  /**
   * @return the associated {@link ValueProvider} for the parameter
   */
  Class<? extends ValueProvider> value();

  /**
   * @return a boolean indicating if this values are closed or not
   */
  boolean open() default true;

}
