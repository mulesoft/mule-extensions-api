/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.source;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@link Source} supports emitting responses back to the client once a generated message is
 * processed (either correctly or incorrectly).
 * <p>
 * Notice that this is a declaration of intent. The {@link Source} <b>MUST</b> use this annotation to communicate that it supports
 * doing that, but the mere fact of using this annotation doesn't mean that the source will do it automatically. It's each
 * source's responsibility to do that correctly
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface EmitsResponse {

}
