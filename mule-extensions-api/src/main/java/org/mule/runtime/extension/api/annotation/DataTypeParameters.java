/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import org.mule.runtime.api.meta.model.operation.OperationModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Indicates that an {@link OperationModel} should expose parameters which
 * allows the user to specify the {@link org.mule.runtime.api.metadata.DataType}
 * of the annotated operation's output.
 * <p>
 * This annotation is intended to be used in {@link Method methods} from which
 * an operation is derived
 *
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataTypeParameters {

}
