/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.execution;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.runtime.source.SourceResult;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an annotated method should be executed when a
 * generated event was processed by the owning flow, doesn't taking in
 * account if the processing finished successfully or not.
 * <p>
 * For {@link Source sources} the unique parameters which this callback can receive are
 * the {@link SourceResult};
 *
 * @since 1.0
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnTerminate {
}
