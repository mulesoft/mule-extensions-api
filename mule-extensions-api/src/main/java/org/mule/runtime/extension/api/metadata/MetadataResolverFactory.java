/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;

import java.util.Collection;

/**
 * Provides instances of the {@link TypeKeysResolver}, {@link TypeKeysResolver},
 * {@link OutputTypeResolver} and {@link QueryEntityResolver} resolving types associated to a Component
 *
 * @since 1.0
 */
public interface MetadataResolverFactory {

  /**
   * Provides an instance of the {@link TypeKeysResolver} type associated to the Component
   *
   * @return an instance of the {@link TypeKeysResolver}
   */
  TypeKeysResolver getKeyResolver();

  /**
   * Provides an instance of the {@link InputTypeResolver} type associated to the Component
   *
   * @return an instance of the {@link InputTypeResolver}
   */
  <T> InputTypeResolver<T> getInputResolver(String parameterName);

  Collection<InputTypeResolver> getInputResolvers();

  /**
   * Provides an instance of the {@link OutputTypeResolver} type associated to the Component
   *
   * @return an instance of the {@link OutputTypeResolver}
   */
  <T> OutputTypeResolver<T> getOutputResolver();

  /**
   * Provides an instance of the {@link AttributesTypeResolver} type associated to the Component
   *
   * @return an instance of the {@link AttributesTypeResolver}
   */
  <T> AttributesTypeResolver<T> getOutputAttributesResolver();

  /**
   * Provides an instance of the {@link QueryEntityResolver} type associated to a query operation.
   *
   * @return a {@link QueryEntityResolver} instance.
   */
  QueryEntityResolver getQueryEntityResolver();
}
