/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.error;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.ErrorTypeDefinition;
import org.mule.runtime.extension.api.annotation.Extension;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares an array of {@link Class}es of an {@link ErrorTypeProvider}, to communicate and declare
 * which {@link ErrorTypeDefinition} the operation could throw.
 *
 * @since 1.0
 * @see ErrorTypeProvider
 * @see ErrorTypeDefinition
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface Throws {

  Class<? extends ErrorTypeProvider>[] value();
}
