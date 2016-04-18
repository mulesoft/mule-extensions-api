/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.Sources;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to specify a custom label for the element and/or field to be used in the UI.
 * If a value is not specified, the name is inferred from the annotated element's name.
 * <p>
 * Use this annotation to override the default inferred nickname
 * for a {@link Parameter}, an operation, an operation's parameter or a {@link Sources}.
 *
 * @since 1.0
 */
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisplayName
{

    /**
     * The value is a short name for the annotated element.
     * If this value is not specified it will be inferred from the annotated element name.
     */
    String value() default "";
}