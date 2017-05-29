/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.execution;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.OnTerminateCallback;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an annotated method should be executed when a given
 * execution finishes.
 *
 * The actual meaning of the term 'given execution' depends of the context in which
 * this annotation is used. For example, if used in a {@link Source} type, it means
 * that the annotated method should be executed when a generated event was processed
 * by the owning flow doesn't taking in account if the processing finished successfully or not.
 *
 * For {@link Source sources} the unique parameters which this callback can receive are
 * the {@link OnTerminateCallback} and the {@link SourceCallbackContext}
 *
 * @since 1.0
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnTerminate {
}
