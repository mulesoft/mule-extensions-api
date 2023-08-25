/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.metadata;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;

import java.util.Collection;

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
   * Provides an instance of the {@link QueryEntityResolver} type associated to a query operation.
   *
   * @return a {@link QueryEntityResolver} instance.
   */
  QueryEntityResolver getQueryEntityResolver();
}
