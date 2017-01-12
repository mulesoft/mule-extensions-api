/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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

public class MetadataResolverUtils {

  public static Optional<String> getCategoryName(MetadataResolverFactory metadataResolverFactory) {
    return getDeclaredResolvers(metadataResolverFactory).stream()
        .map(NamedTypeResolver::getCategoryName)
        .filter(name -> !isBlank(name))
        .findFirst();

  }

  public static List<NamedTypeResolver> getAllResolvers(MetadataResolverFactory metadataResolverFactory) {
    List<NamedTypeResolver> resolvers = new LinkedList<>();
    resolvers.add(metadataResolverFactory.getKeyResolver());
    resolvers.add(metadataResolverFactory.getOutputResolver());
    resolvers.add(metadataResolverFactory.getOutputAttributesResolver());
    resolvers.add(metadataResolverFactory.getQueryEntityResolver());
    resolvers.addAll(metadataResolverFactory.getInputResolvers());

    return resolvers;
  }

  public static List<NamedTypeResolver> getDeclaredResolvers(MetadataResolverFactory metadataResolverFactory) {
    return getAllResolvers(metadataResolverFactory).stream()
        .filter(r -> !isNullResolver(r))
        .collect(toList());
  }

  public static boolean isNullResolver(NamedTypeResolver resolver) {
    return resolver.getClass().equals(NullMetadataResolver.class)
        || resolver.getClass().equals(NullQueryMetadataResolver.class);
  }
}
