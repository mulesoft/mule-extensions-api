/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.extension.api.metadata.NullMetadataResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associates the annotated Component to an {@link OutputTypeResolver} that will be used
 * to resolve the Component's return {@link MetadataType type} dynamically
 *
 * @since 1.0
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface OutputResolver {

  /**
   * @return the associated {@link OutputTypeResolver} for the annotated Component
   */
  Class<? extends OutputTypeResolver> output() default NullMetadataResolver.class;

  /**
   * @return the associated {@link AttributesTypeResolver} for the annotated Component
   */
  Class<? extends AttributesTypeResolver> attributes() default NullMetadataResolver.class;
}
