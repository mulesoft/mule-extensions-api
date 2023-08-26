/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows to specify an example for a {@link EnrichableModel} to be used in the UI. This example is not related to the
 * {@link Optional#defaultValue()} used, it's only for the purpose of showing how does a possible value looks like.
 * 
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Example {

  /**
   * @return The given example
   */
  String value();
}
