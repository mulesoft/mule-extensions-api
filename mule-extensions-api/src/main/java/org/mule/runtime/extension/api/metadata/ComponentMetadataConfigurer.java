/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import org.mule.api.annotation.Experimental;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclarer;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.sdk.api.metadata.resolving.ChainInputTypeResolver;

import java.util.Map;


/**
 * Declarative API for configuring DataSense related resolvers at the declarer or declaration level with minimal exposure to the
 * implementation details.
 *
 * <b>NOTE:</b> Experimental feature. Backwards compatibility is not guaranteed.
 *
 * @since 1.8
 */
@Experimental
public interface ComponentMetadataConfigurer {

  /**
   * Configures the given {@code declaration} with resolvers that implement the {@code Null-Object} design pattern. That is, the
   * declaration will get enriched with resolver instances that yield default (non necessarily useful) values.
   *
   * @param declaration a component's declaration object
   */
  <T extends ParameterizedDeclaration> void configureNullMetadata(ParameterizedDeclaration<T> declaration);

  /**
   * Configures the given {@code declarer} with resolvers that implement the {@code Null-Object} design pattern. That is, the
   * declaration will get enriched with resolver instances that yield default (non necessarily useful) values.
   *
   * @param declarer a component's declarer object
   */
  <T extends ParameterizedDeclarer, D extends ParameterizedDeclaration> void configureNullMetadata(ParameterizedDeclarer<T, D> declarer);


  /**
   * Sets an {@link OutputTypeResolver}
   *
   * @param outputTypeResolver the configured resolver
   * @return {@code this} instance
   * @throws IllegalArgumentException if {@code outputTypeResolver} is {@code null}
   */
  ComponentMetadataConfigurer setOutputTypeResolver(OutputTypeResolver outputTypeResolver);

  /**
   * Sets a {@link AttributesTypeResolver}
   *
   * @param attributesTypeResolver the configured resolver
   * @return {@code this} instance
   * @throws IllegalArgumentException if {@code attributesTypeResolver} is {@code null}
   */
  ComponentMetadataConfigurer setAttributesTypeResolver(AttributesTypeResolver attributesTypeResolver);

  /**
   * Sets a {@link TypeKeysResolver}
   *
   * @param keysResolver         the configured resolver
   * @param keyParameterName     the name of the parameter acting as the metadata key
   * @param keyParameterType     the type of the parameter referenced by the {@code keyParameterName} argument
   * @param isPartialKeyResolver whether this resolver is a partial key resolver
   * @return {@code this} instance
   * @throws IllegalArgumentException if {@code keysResolver} or {@code keyParameterType} are {@code null}, or
   *                                  {@code keyParameterName} is blank
   */
  ComponentMetadataConfigurer setKeysResolver(TypeKeysResolver keysResolver, String keyParameterName,
                                              MetadataType keyParameterType, boolean isPartialKeyResolver);

  /**
   * Sets a {@link ChainInputTypeResolver}. Only use when configuring a scope component.
   *
   * @param chainInputTypeResolver the configured resolver
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer setChainInputTypeResolver(ChainInputTypeResolver chainInputTypeResolver);

  /**
   * Convenience method to configure a scope/router whose inner chain/s will receive the same input type of the scope/router
   * itself. An example of such a scope would be {@code <async>}.
   *
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer withPassThroughChainInputTypeResolver();

  /**
   * Adds an {@link InputTypeResolver} for a specific input parameter
   *
   * @param parameterName the resolved parameter name
   * @param resolver      the configured resolver
   * @return {@code this} instance
   * @throws IllegalArgumentException if {@code parameterName} is blank or {@code resolver} is {@code null}
   */
  ComponentMetadataConfigurer addInputResolver(String parameterName, InputTypeResolver resolver);

  /**
   * Invokes {@link #addInputResolver(String, InputTypeResolver)} per each entry in {@code resolvers}
   *
   * @param resolvers the resolvers to add.
   * @return {@code this} instance
   * @throws IllegalArgumentException under any of the circumstances that {@link #addInputResolver(String, InputTypeResolver)}
   *                                  would
   */
  ComponentMetadataConfigurer addInputResolvers(Map<String, InputTypeResolver> resolvers);

  /**
   * Adds a {@link ChainInputTypeResolver} for a specific route. Only use when configuring router components
   *
   * @param routeName the route name
   * @param resolver  the resolver being set
   * @return {@code this} instance
   * @throws IllegalArgumentException if {@code routeName} is blank or {@code resolver} is {@code null}
   */
  ComponentMetadataConfigurer addRouteChainInputResolver(String routeName, ChainInputTypeResolver resolver);

  /**
   * Invokes {@link #addRouteChainInputResolver(String, ChainInputTypeResolver)} per each entry in {@code resolvers}
   *
   * @param resolvers the resolvers to add.
   * @return {@code this} instance
   * @throws IllegalArgumentException under any of the circumstances that
   *                                  {@link #addRouteChainInputResolver(String, ChainInputTypeResolver) would
   */
  ComponentMetadataConfigurer addRoutesChainInputResolvers(Map<String, ChainInputTypeResolver> resolvers);

  /**
   * Whether any of the configured resolvers require a connection. If not invoked, all resolvers will be assumed to not require
   * it.
   *
   * @param connected whether any of the configured resolvers require a connection
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer setConnected(boolean connected);

  /**
   * Convenience method to configure routers that will output the result of (any) one of its routes. An example of such a router
   * would be {@code <first-successful>}
   *
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer asOneOfRouter();

  /**
   * Convenience method to configure a scope that outputs the result of its inner chain. An example of such a scope would be
   * {@code <try>}
   *
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer asPassthroughScope();

  /**
   * Convenience method to configure routers that return the sum of all its routes, in the form of an object which attributes
   * matches the route names (e.g: {@code <scatter-gather>}
   *
   * @return {@code this} instance
   */
  ComponentMetadataConfigurer asAllOfRouter();

  /**
   * Applies the configuration on {@code this} instance on the given {@code declarer}.
   *
   * @param declarer the target declarer
   */
  <T extends ParameterizedDeclarer, D extends ParameterizedDeclaration> void configure(ParameterizedDeclarer<T, D> declarer);

  /**
   * Applies the configuration on {@code this} instance on the given {@code declaration}.
   *
   * @param declaration the target declaration
   */
  <T extends ComponentDeclaration> void configure(ParameterizedDeclaration<T> declaration);

}
