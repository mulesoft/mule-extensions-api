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
 * This annotation marks that the {@link Parameter} within the annotated class have an exclusive relation among each other.
 *
 * The exclusive relation stands for "from all the parameters declared in this class, only one can be present at any time"
 *
 * Not parameter being present is a valid scenario. If the situation was, in which at least one of
 * the parameters must be present at any given time, the {@code atLeastOneIsRequired} must be set to {@code true}.
 *
 * This annotation overrides the optionality of the {@link Parameter} within the annotated, forcing all of them
 * to be treated as optionals.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exclusion
{

    /**
     * Enforces whether if at least one of the parameters must be present at any given time
     */
    boolean atLeastOneIsRequired() default false;
}