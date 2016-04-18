/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.metadata.resolving.MetadataContentResolver;
import org.mule.metadata.api.model.MetadataType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks one of the arguments of an Operation's method as it's main input.
 * The {@link MetadataType} of this annotated parameter can be resolved by {@link MetadataContentResolver}
 * referenced in the {@link MetadataScope} annotation at Operation or Extension level, if one is present.
 *
 * @since 1.0
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface Content
{

}
