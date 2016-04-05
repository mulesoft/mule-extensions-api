/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Annotation to be used on an {@link Extension} to declare the implementations
 * of a given {@code abstract} {@link Class} or {@code interface}.
 * Then when a {@link Parameter} or {@link Field} of this base type is found,
 * xml support for declaring the sub types implementations will be generated.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SubtypesMapping.class)
public @interface SubtypeMapping
{

    Class<?> baseType();

    Class<?>[] subTypes();

}
