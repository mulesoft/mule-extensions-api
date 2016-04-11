/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.api.metadata.MetadataKey;
import org.mule.api.metadata.resolving.MetadataKeysResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks one of the arguments of an Operation or or Source's method as the key for the metadata lookup.
 * This lookup is resolved by the {@link MetadataKeysResolver} referenced in the {@link MetadataScope},
 * at Operation, Source or Extension level, which will return the list of available the {@link MetadataKey}.
 * <br/>
 * At design time, the selected {@link MetadataKey} of the annotated parameter will be taken as the key to provide
 * metadata for the parameter annotated with {@link Content} in a Operation and/or for output metadata of an Operation
 * or Source.
 *
 * @since 1.0
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface MetadataKeyParam
{

}
