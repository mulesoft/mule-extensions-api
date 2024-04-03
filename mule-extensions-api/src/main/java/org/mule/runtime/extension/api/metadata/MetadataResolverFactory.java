/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;

import org.mule.api.annotation.Experimental;
import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.sdk.api.annotation.MinMuleVersion;
import org.mule.sdk.api.metadata.resolving.ChainInputTypeResolver;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Provides instances of the {@link TypeKeysResolver}, {@link TypeKeysResolver}, {@link OutputTypeResolver} and
 * {@link QueryEntityResolver} resolving types associated to a Component
 *
 * @since 1.0
 */
@NoImplement
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

  /**
   * Provides all the {@link InputTypeResolver} associated to the parameters of the Component.
   *
   * @return a {@link Collection} of {@link InputTypeResolver}
   */
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
   * <b>NOTE:</b> Experimental feature. Backwards compatibility is not guaranteed.
   *
   * @return an optional {@link ChainInputTypeResolver} associated to the component.
   * @since 1.7.0
   */
  @Experimental
  @MinMuleVersion("4.7.0")
  default Optional<ChainInputTypeResolver> getScopeChainInputTypeResolver() {
    return empty();
  }

  /**
   * If the component is a router, it provides {@link ChainInputTypeResolver} instances
   * through a {@link Map} which keys are the corresponding route names. If the component is not a router,
   * an empty map will be returned.
   *
   * <b>NOTE:</b> Experimental feature. Backwards compatibility is not guaranteed.
   *
   * @return an unmodifiable map. Cannot be {@code null}
   * @since 1.7.0
   */
  @Experimental
  @MinMuleVersion("4.7.0")
  default Map<String, ChainInputTypeResolver> getRouterChainInputResolvers() {
    return emptyMap();
  }

  /**
   * Provides an instance of the {@link QueryEntityResolver} type associated to a query operation.
   *
   * @return a {@link QueryEntityResolver} instance.
   */
  QueryEntityResolver getQueryEntityResolver();
}
