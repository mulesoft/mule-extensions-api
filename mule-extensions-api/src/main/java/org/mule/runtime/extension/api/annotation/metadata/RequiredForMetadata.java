/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation which indicates that the annotated parameter is required for Metadata, this indicates that
 * any other not annotated parameter won't be considered as part of the metadata resolution, so if the value
 * changes the metadata resolution won't be triggered for those connectors.
 * <p/>
 * This annotation can be only used on Configuration and Connection Provider parameters.
 *
 * @since 1.2.0
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface RequiredForMetadata {

}
