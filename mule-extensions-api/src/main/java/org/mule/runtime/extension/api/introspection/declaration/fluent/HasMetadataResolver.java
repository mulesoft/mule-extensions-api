/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;

/**
 * A contract interface for an object that has a {@link MetadataResolverFactory}
 * associated for Metadata retrieval
 *
 * @param <T> the type of the implementing type. Used to allow method chaining
 * @since 1.0
 */
interface HasMetadataResolver<T> {

  /**
   * Sets the given {@code metadataResolverFactory}
   *
   * @param metadataResolverFactory the {@link MetadataResolverFactory} associated to the enriched component
   * @return {@code this} declarer
   */
  T withMetadataResolverFactory(MetadataResolverFactory metadataResolverFactory);

}
