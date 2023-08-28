/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.Collections.emptySet;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Collections;
import java.util.Set;

/**
 * Null implementation of {@link InputTypeResolver}, {@link AttributesTypeResolver} and {@link TypeKeysResolver}, used to
 * represent the absence of any of them when required.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public final class NullMetadataResolver implements InputTypeResolver<Object>, TypeKeysResolver,
    OutputTypeResolver<Object>, AttributesTypeResolver<Object> {

  public static final String NULL_CATEGORY_NAME = "NullCategory";
  public static final String NULL_RESOLVER_NAME = "NullResolver";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResolverName() {
    return NULL_RESOLVER_NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategoryName() {
    return NULL_CATEGORY_NAME;
  }

  /**
   * Null implementation of {@link TypeKeysResolver}, used when no implementation is provided by the connector developer.
   * Represents the absence of a custom {@link TypeKeysResolver}, returning an empty list of {@link MetadataKey}.
   *
   * @param context {@link MetadataContext} of the MetaData resolution
   * @return {@link Collections#emptyList()}
   * @throws MetadataResolvingException
   */
  @Override
  public Set<MetadataKey> getKeys(MetadataContext context) throws MetadataResolvingException {
    return emptySet();
  }

  /**
   * Null implementation of {@link InputTypeResolver}, used when no implementation is provided by the connector developer.
   * Represents the absence of a custom {@link InputTypeResolver}, returning a {@link VoidType} instead of resolving a valid
   * {@link MetadataType}
   *
   * @param context {@link MetadataContext} of the MetaData resolution
   * @param key     {@link MetadataKey} of the type which's structure has to be resolved
   * @return a {@link VoidType}
   * @throws MetadataResolvingException
   */
  @Override
  public MetadataType getInputMetadata(MetadataContext context, Object key) throws MetadataResolvingException {
    return context.getTypeBuilder().voidType().build();
  }

  /**
   * Null implementation of {@link OutputTypeResolver}, used when no implementation is provided by the connector developer.
   * Represents the absence of a custom {@link OutputTypeResolver}, returning a {@link VoidType} instead of resolving a dynamic
   * {@link MetadataType} for the component's output.
   *
   * @param context {@link MetadataContext} of the MetaData resolution
   * @param key     {@link MetadataKey} of the type which's structure has to be resolved
   * @return a {@link VoidType}
   * @throws MetadataResolvingException
   */
  @Override
  public MetadataType getOutputType(MetadataContext context, Object key) throws MetadataResolvingException {
    return context.getTypeBuilder().voidType().build();
  }

  /**
   * Null implementation of {@link AttributesTypeResolver}, used when no implementation is provided by the connector developer.
   * Represents the absence of a custom {@link AttributesTypeResolver}, returning a {@link VoidType} instead of resolving a
   * dynamic {@link MetadataType} for the component's output attributes.
   *
   * @param context {@link MetadataContext} of the MetaData resolution
   * @param key     {@link MetadataKey} of the type which's structure has to be resolved
   * @return a {@link VoidType}
   * @throws MetadataResolvingException
   */
  @Override
  public MetadataType getAttributesType(MetadataContext context, Object key) throws MetadataResolvingException {
    return context.getTypeBuilder().voidType().build();
  }
}
