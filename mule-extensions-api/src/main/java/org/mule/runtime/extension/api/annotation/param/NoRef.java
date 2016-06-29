/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import org.mule.runtime.extension.api.annotation.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated member does not accept static references to
 * objects in the mule registry. The most common use case is to allow parameters
 * of type {@link Object} to be assigned to a static {@link String} value, without
 * it representing a reference to an object in the registry, but actually representing
 * the static String itself. For example:
 * <pre>
 *     public void send(@NoRef Object content) {}
 * </pre>
 * <p>
 * Or:
 * <p>
 * <pre>
 *     public class EmailContent {
 *
 *     @Parameter
 *     @NoRef
 *     private Object content
 *     }
 *
 * </pre>
 * <p>
 * <p>
 * This annotation can either be applied to an argument of an operation method
 * or to a field annotated with {@link Parameter}.
 *
 * @since 1.0
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRef
{

}
