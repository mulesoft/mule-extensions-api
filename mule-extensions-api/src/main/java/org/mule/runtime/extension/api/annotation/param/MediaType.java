/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.io.InputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use this annotation on operation methods and {@link Source} classes which return either {@link String} or {@link InputStream}.
 *
 * Because those types can carry data on any format, and in order to guarantee interoperability with DataWeave, such components
 * need to carry this operation specifying the output mime type, even if it's {@link #ANY}.
 *
 * The output value's mimeType will be automatically set to it. Plus, depending on the value of {@link #strict()}, the runtime may
 * also add synthetic parameters to allow the user to specify a different one.
 *
 * Operations or sources which return {@link String} or {@link InputStream} and do not carry this annotation will fail to compile.
 *
 * For convenience, this class also defines a set of constants with the most common mime types.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface MediaType {

  /**
   * The mime type in RFC format. This needs to be a mime type without any custom parameters nor encoding. If specified, those
   * will be ignored. The runtime will only consider the primary and sub types.
   */
  String value() default "";

  /**
   * Whether the component must only return values of the given mimeType ({@code true}), or if the user should be given the chance
   * to override it with its own
   */
  boolean strict() default true;

  String APPLICATION_PLAIN = "application/plain";
  String APPLICATION_JSON = "application/json";
  String APPLICATION_XML = "application/xml";
  String APPLICATION_HTML = "application/html";
  String APPLICATION_CSV = "application/csv";
  String APPLICATION_OCTET_STREAM = "application/octet-stream";
  String TEXT_PLAIN = "text/plain";
  String ANY = "*/*";

}
