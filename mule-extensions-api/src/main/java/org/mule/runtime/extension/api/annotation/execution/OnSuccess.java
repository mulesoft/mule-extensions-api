/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.execution;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an Source's method should be executed when a generated event is successfully processed by the owning
 * flow.
 * <p>
 * Another important semantic of this annotation, is that whatever parameters the annotated method takes, are to be automatically
 * resolved by the runtime in the same way as an operation method would.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnSuccess {

}
