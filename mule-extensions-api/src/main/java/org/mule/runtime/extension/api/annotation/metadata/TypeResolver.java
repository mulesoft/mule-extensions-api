/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associates the annotated Parameter to an {@link InputTypeResolver} that will be used to resolve the Parameter's
 * {@link MetadataType} dynamically
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.metadata.TypeResolver} instead.
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface TypeResolver {

  /**
   * @return the associated {@link InputTypeResolver} for the annotated Parameter
   */
  Class<? extends InputTypeResolver> value();

}
