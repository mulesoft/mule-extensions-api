/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is meant to be applied on fields of classes
 * which are serving as extension configurations.
 * <p>
 * This annotation signals that the target field should be injected
 * with the name that the config has received on the application configuration.
 * <p>
 * This implies the following restrictions:
 * <p>
 * <lu>
 * <li>The field should be of type {@link String}</li>
 * <li>No two fields in the same class should bear this annotation</li>
 * </lu>
 *
 * @since 1.0
 */
@Target(value = FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigName
{

}
