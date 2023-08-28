/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import org.mule.runtime.api.metadata.resolving.NamedTypeResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Helper methods for the {@link MetadataResolverFactory}
 * 
 * @since 1.0
 */
public abstract class MetadataResolverUtils {

  private MetadataResolverUtils() {}

  /**
   * Returns the first not blank category name from declared resolvers
   */
  public static Optional<String> getCategoryName(MetadataResolverFactory metadataResolverFactory) {
    return getDeclaredResolvers(metadataResolverFactory).stream()
        .map(NamedTypeResolver::getCategoryName)
        .filter(name -> !isBlank(name))
        .findFirst();

  }

  /**
   * Returns all the resolvers from the factory
   */
  public static List<NamedTypeResolver> getAllResolvers(MetadataResolverFactory metadataResolverFactory) {
    List<NamedTypeResolver> resolvers = new LinkedList<>();
    resolvers.add(metadataResolverFactory.getKeyResolver());
    resolvers.add(metadataResolverFactory.getOutputResolver());
    resolvers.add(metadataResolverFactory.getOutputAttributesResolver());
    resolvers.add(metadataResolverFactory.getQueryEntityResolver());
    resolvers.addAll(metadataResolverFactory.getInputResolvers());

    return resolvers;
  }

  /**
   * Returns a list of all the factory resolvers which are not {@link MetadataResolverUtils#isNullResolver(NamedTypeResolver)}
   */
  public static List<NamedTypeResolver> getDeclaredResolvers(MetadataResolverFactory metadataResolverFactory) {
    return getAllResolvers(metadataResolverFactory).stream()
        .filter(r -> !isNullResolver(r))
        .collect(toList());
  }

  /**
   * Determines whether a resolver is a null resolver implementation or not.
   */
  public static boolean isNullResolver(NamedTypeResolver resolver) {
    return resolver.getClass().equals(NullMetadataResolver.class)
        || resolver.getClass().equals(org.mule.sdk.api.metadata.NullMetadataResolver.class)
        || resolver.getClass().equals(NullQueryMetadataResolver.class);
  }
}
