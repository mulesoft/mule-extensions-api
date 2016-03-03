/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.annotation;

import org.mule.extension.api.annotation.capability.Xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that a given type can be extended by others.
 * <p/>
 * The actual implications of 'can be extended' depends on the context. For example, in the case of an extension
 * annotated with {@link Xml}, it means that this extensible member will generate a substitution group that implementing
 * ones will be a part of.
 * <p/>
 * This annotation is meant to be used on classes holding methods annotated
 * with {@link Operation} and all operations defined on that class will be considerable
 * extensible.
 * <p/>
 * Implementation of this extensible type are marked using the {@link ExtensionOf}
 * annotation
 *
 * @see {@link ExtensionOf}
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extensible
{

    /**
     * An optional alias for the type
     */
    String alias() default "";
}
