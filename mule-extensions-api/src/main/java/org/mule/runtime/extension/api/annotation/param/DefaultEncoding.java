/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is meant to be applied on fields of classes or on operation's arguments.
 * <p>
 * This annotation signals that the target field should be injected with Mule's default encoding.
 * <p>
 *
 * The annotated field should be of type {@link String}
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.param.DefaultEncoding} instead.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface DefaultEncoding {

}
