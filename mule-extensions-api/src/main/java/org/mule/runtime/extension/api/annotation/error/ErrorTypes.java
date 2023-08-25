/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.error;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for {@link Extension} level to communicate and declare the {@link ErrorTypeDefinition}s that the whole extension
 * manages
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface ErrorTypes {

  Class<? extends Enum<? extends ErrorTypeDefinition<? extends Enum>>> value();
}
