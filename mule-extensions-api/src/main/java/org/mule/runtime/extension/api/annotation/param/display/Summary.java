/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows to specify a summary for a {@link EnrichableModel} to be used in the UI. If a value is not specified, the name is
 * inferred from the annotated element's name.
 * <p>
 * Use this annotation to override the default inferred name of the {@link EnrichableModel}
 *
 * @since 1.0
 */
@MinMuleVersion("4.0")
@Target({PARAMETER, FIELD, TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Summary {

  /**
   * @return The given summary text
   */
  String value();
}
