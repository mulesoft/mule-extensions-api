/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.reference;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used in a {@link String} type {@link Parameter}
 * field or parameter that is a reference to a flow in a mule app.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.param.reference.FlowReference} instead.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface FlowReference {

}
