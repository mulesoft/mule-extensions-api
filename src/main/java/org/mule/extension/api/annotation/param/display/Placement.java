/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.annotation.param.display;

import org.mule.extension.api.annotation.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the particular place of a {@link Parameter} field
 * in the extension configuration window.
 *
 * @since 1.0
 */
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Placement {

    int DEFAULT_ORDER = 1;

    /**
     * Gives the annotated element a relative order within its group. The value provided may be repeated
     * and in that case the order is not guaranteed.
     *
     * The value is relative meaning that the element with order 10 is on top than one with value 25.
     */
    int order() default DEFAULT_ORDER;

    /**
     * A group is a logical way to display one or more variables together. If no group is specified then a
     * default group is assumed.
     *
     * To place more than one element in the same group, use the exact same values for this attribute
     */
    String group() default "";

    /**
     * A tab is a logical way to groups together. This attributes specifies the name of the tab in which the
     * annotated element should be displayed. If no tab is specified then a default tab is assumed.
     *
     * To display more than one parameter or field in the same the tab then this value should be exactly the same for all of them.
     */
    String tab() default "";
}
