/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.execution;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an annotated method should be executed when a generated event was processed by the owning flow but an
 * error was thrown.
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
public @interface OnError {


}
