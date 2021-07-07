/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

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
 * Intended to be used on operation methods and implementations of {@link Source}, this annotation indicates that such component
 * supports streaming.
 * <p>
 * The runtime can automatically infer that when the return type is a {@link InputStream}, but there're corner cases in which the
 * return type is of some other abstraction type and thus this has to be manually hinted.
 * <p>
 * Using this annotation on components which return {@link InputStream} is redundant yet harmless.
 *
 * @since 1.0
 */
@MinMuleVersion("4.0")
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface Streaming {

}
