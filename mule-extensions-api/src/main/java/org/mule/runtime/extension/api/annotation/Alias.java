/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an alias for a Java type. Implementations of the
 * extensions API can use this annotation in many ways, but
 * in general terms it's useful to allow a Java type to
 * have a name which makes sense from a coding point of view
 * while maintaining a user friendly name in the places
 * where the extensions API exposes such type
 *
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias
{

    /**
     * @return The alias of the annotated member
     */
    String value();

    /**
     * An optional description to further describe the annotated
     * member
     *
     * @return a nullable {@link String}
     */
    String description() default "";
}
