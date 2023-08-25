/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.execution;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceResult;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an annotated method should be executed when a generated event was processed by the owning flow, doesn't
 * taking in account if the processing finished successfully or not.
 * <p>
 * For {@link Source sources} the unique parameters which this callback can receive are the {@link SourceResult};
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnTerminate {
}
