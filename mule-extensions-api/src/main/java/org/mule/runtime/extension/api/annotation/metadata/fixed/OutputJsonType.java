/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.annotation.metadata.fixed;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Declares the annotated {@link ComponentModel}'s output {@link MetadataType} to the type represented by the JSON Schema.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface OutputJsonType {

  /**
   * @return a JSON schema that describes the type structure of the output. The schema must live in the extension resources in
   *         order to be located.
   */
  String schema();
}
