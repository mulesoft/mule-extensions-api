/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignores a field or method inside a complex type. Only usable in complex types. The purpose is to signal that a given field or
 * setter should not be used when building instances of a class
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.Ignore} instead.
 */
@Target(value = {FIELD, METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Ignore {

}

