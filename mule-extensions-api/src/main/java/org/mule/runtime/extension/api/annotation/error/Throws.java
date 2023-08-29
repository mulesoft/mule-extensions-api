/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.error;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares an array of {@link Class classes} of an {@link ErrorTypeProvider}, to communicate and declare which
 * {@link ErrorTypeDefinition} the operation could throw.
 * <p>
 * This annotation can be applied to an Operation method, an Operation container class or an {@link Extension} class. The
 * annotation value to consider is the one of in the deepest level, eg: If the annotation is used in an operation method and in an
 * extension class, the one to use is the one in the operation.
 *
 * @since 1.0
 * @see ErrorTypeProvider
 * @see ErrorTypeDefinition
 */
@MinMuleVersion("4.1")
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface Throws {

  Class<? extends ErrorTypeProvider>[] value();
}
