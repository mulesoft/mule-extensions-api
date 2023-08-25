/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associates the annotated Parameter to an {@link InputTypeResolver} that will be used to resolve the Parameter's
 * {@link MetadataType} dynamically
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface TypeResolver {

  /**
   * @return the associated {@link InputTypeResolver} for the annotated Parameter
   */
  Class<? extends InputTypeResolver> value();

}
