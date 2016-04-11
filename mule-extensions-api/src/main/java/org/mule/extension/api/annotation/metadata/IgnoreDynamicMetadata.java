/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.extension.api.annotation.Extension;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated method will not use any inherited {@link MetadataScope} resolver. This annotation provides
 * a way of avoiding the use of resolvers inherited from the class declaring the method or the {@link Extension} itself.
 *
 * @since 1.0
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface IgnoreDynamicMetadata
{

}
