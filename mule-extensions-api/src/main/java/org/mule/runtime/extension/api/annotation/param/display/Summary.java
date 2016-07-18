/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows to specify a summary for the parameter, field, type or method to be used in the UI.
 * If a value is not specified, the name is inferred from the annotated element's name.
 * <p>
 * Use this annotation to override the default inferred nickname for a {@link Parameter}, an operation, an operation's
 * parameter, {@link Source}, {@link Configuration} and {@link ConnectionProvider}.
 *
 * @since 1.0
 */
@Target({PARAMETER, FIELD, TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Summary
{

    /**
     * @return The given summary text
     */
    String value();
}
