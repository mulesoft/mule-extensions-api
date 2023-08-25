/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to express that the annotated element should not support being used implicitly.
 *
 * @since 1.3.0
 */
@MinMuleVersion("4.3")
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface NoImplicit {

}
