/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.extension.api.introspection.ExtensionModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation defines a class that will export its functionality as a Mule module.
 * <p/>
 * There are a few restrictions as to which types as valid for this annotation:
 * - It cannot be an interface
 * - It must be public
 * - It cannot have a typed parameter (no generic)
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension
{

    /**
     * A name consistent with the definition on {@link ExtensionModel#getName()}
     */
    String name();

    /**
     * Short description about the extension's functionality
     */
    String description() default "";

    /**
     * Name of the extension's vendor
     */
    String vendor() default NO_VENDOR;

    String NO_VENDOR = "";

    String DEFAULT_CONFIG_NAME = "config";

    String DEFAULT_CONFIG_DESCRIPTION = "Default configuration";
}