/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker annotation which indicates that the annotated parameter is required for Metadata, this indicates that any other not
 * annotated parameter won't be considered as part of the metadata resolution, so if the value changes the metadata resolution
 * won't be triggered for those connectors
 * </p>
 * This parameter has the same effect when getting values from a operation's
 * {@link org.mule.runtime.extension.api.values.ValueProvider}. Any config or connection parameter that affects the resolution of
 * a {@link org.mule.runtime.extension.api.values.ValueProvider} that is linked to an operation must be annotated with this
 * parameter in order to trigger resolution if it's value changed.
 * <p/>
 * This annotation can be only used on Configuration and Connection Provider parameters.
 *
 * @since 1.2.0
 */
@MinMuleVersion("4.2")
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface RequiredForMetadata {

}
