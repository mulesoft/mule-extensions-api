/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link Parameter} field as one that supports a multi line string input both in the editor (when it is populated from
 * the UI) and in the DSL.
 * <p/>
 * This annotation should only be used with {@link String} parameters.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.param.display.Text} instead.
 */
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Text {

}
