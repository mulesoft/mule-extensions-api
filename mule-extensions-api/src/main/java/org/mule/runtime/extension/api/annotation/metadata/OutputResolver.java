/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.extension.api.metadata.NullMetadataResolver;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associates the annotated Component to an {@link OutputTypeResolver} that will be used to resolve the Component's return
 * {@link MetadataType type} dynamically
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface OutputResolver {

  /**
   * Specifies the {@link OutputTypeResolver} which should be used to resolve the type of the value which will go into the payload
   * of the output message.
   * <p>
   * Always keep in mind that this should resolve to a single unitary type. That means that for operations which return
   * collections, this should not return an {@link ArrayType} but the type of the items inside the collection.
   * <p>
   * So, if the operation returns {@code List<Apple>}, this should return a {@link MetadataType} for the {@code Apple}. Also, if
   * the operation returns {@code List<Result<Apple, AppleAttributes>>}, it still should only return the {@code Apple's} metadata.
   *
   * @return the associated {@link OutputTypeResolver} for the annotated Component
   */
  Class<? extends OutputTypeResolver> output() default NullMetadataResolver.class;

  /**
   * Specifies the {@link AttributesTypeResolver} which should be used to resolve the type of the {@link Message#getAttributes()}
   * of the output message.
   * <p>
   * Always consider that this resolver should be consistent with the one returned by {@link #output()}.
   * <p>
   * So, if the operation returns {@code List<Apple>}, this should return a {@link MetadataType} for attributes which describe the
   * {@code Apple}, not the list. . Also, if the operation returns {@code List<Result<Apple, AppleAttributes>>}, it still should
   * only describe {@code AppleAttributes}.
   *
   * @return the associated {@link AttributesTypeResolver} for the annotated Component
   */
  Class<? extends AttributesTypeResolver> attributes() default NullMetadataResolver.class;
}
