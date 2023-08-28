/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata.fixed;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Declares the annotated {@link ParameterModel}'s {@link MetadataType} to the type represented by the provided JSON Schema.
 * <p>
 * Can only be used on {@link Map}, {@link String} or {@link InputStream} parameters in order to be correctly coerced.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Retention(RUNTIME)
@Target({PARAMETER})
public @interface InputJsonType {

  /**
   * @return a JSON schema that describes the type structure. The schema must live in the extension resources in order to be
   *         located.
   */
  String schema();
}
