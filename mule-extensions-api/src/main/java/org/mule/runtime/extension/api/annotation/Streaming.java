/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
@MinMuleVersion("4.1")
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface Streaming {

}
