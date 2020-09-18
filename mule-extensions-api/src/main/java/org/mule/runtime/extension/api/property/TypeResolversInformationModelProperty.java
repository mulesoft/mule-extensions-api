/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.mule.runtime.extension.api.metadata.NullMetadataResolver.NULL_RESOLVER_NAME;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ModelProperty} for a of {@link ComponentModel} that contains all the information regarding
 * which dynamic {@link TypeResolver}s are used by this component.
 *
 * @since 1.2.0
 */
public class TypeResolversInformationModelProperty implements ModelProperty {

  public static final String NAME = "typeResolversInformation";

  private final String category;
  private final ResolverInformation outputResolver;
  private final ResolverInformation attributesResolver;
  private final ResolverInformation keysResolver;
  private final boolean partialTypeKeyResolver;
  private final Map<String, ResolverInformation> inputResolvers;
  private final transient boolean requiresConnection;
  private final transient boolean requiresConfiguration;

  public TypeResolversInformationModelProperty(String category,
                                               Map<String, String> parameters,
                                               String outputResolver,
                                               String attributesResolver,
                                               String keysResolver,
                                               boolean requiresConnection,
                                               boolean requiresConfiguration) {
    this(category, parameters, outputResolver, attributesResolver, keysResolver, requiresConnection, requiresConfiguration,
         false);
  }

  public TypeResolversInformationModelProperty(String category,
                                               Map<String, String> parameters,
                                               String outputResolver,
                                               String attributesResolver,
                                               String keysResolver,
                                               boolean requiresConnection,
                                               boolean requiresConfiguration,
                                               boolean partialTypeKeyResolver) {
    this.requiresConnection = requiresConnection;
    this.requiresConfiguration = requiresConfiguration;
    checkArgument(isNotBlank(category), "A Category name is required for a group of resolvers");
    this.category = category;
    Map<String, String> paramResolvers = parameters != null && parameters.isEmpty() ? null : parameters;

    this.outputResolver = getResolverInformation(outputResolver);
    this.attributesResolver = getResolverInformation(attributesResolver);
    this.keysResolver = getResolverInformation(keysResolver);

    if (paramResolvers != null) {
      this.inputResolvers = new HashMap<>();
      paramResolvers
          .forEach((paramName, resolverName) -> this.inputResolvers.put(paramName, getResolverInformation(resolverName)));
    } else {
      this.inputResolvers = null;
    }

    this.partialTypeKeyResolver = partialTypeKeyResolver;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  /**
   * Provides the name of the metadata category associated to the Component.
   *
   * @return category name
   */
  public String getCategoryName() {
    return category;
  }

  /**
   * Provides information of the output resolver (if any) associated to the Component.
   *
   * @return output resolver's {@link ResolverInformation}
   */
  public Optional<ResolverInformation> getOutputResolver() {
    return ofNullable(outputResolver);
  }

  /**
   * Provides information the output attributes resolver (if any) associated to the Component.
   *
   * @return output attributes resolver's {@link ResolverInformation}
   */
  public Optional<ResolverInformation> getAttributesResolver() {
    return ofNullable(attributesResolver);
  }

  /**
   * Provides information of the metadata keys resolver (if any) associated to the Component.
   *
   * @return keys resolver's {@link ResolverInformation}
   */
  public Optional<ResolverInformation> getKeysResolver() {
    return ofNullable(keysResolver);
  }

  /**
   * @return boolean indicating if key resolver is partial of complete.
   */
  public boolean isPartialTypeKeyResolver() {
    return partialTypeKeyResolver;
  }

  /**
   * Provides information of the resolver (if any) associated to a given parameter.
   *
   * @param parameterName name of the parameter
   * @return {@link ResolverInformation} of the resolver associated to the parameter, if one exists.
   */
  public Optional<ResolverInformation> getParameterResolver(String parameterName) {
    return inputResolvers != null ? ofNullable(inputResolvers.get(parameterName)) : empty();
  }

  private String sanitizeResolverName(String resolverName) {
    return isAllBlank(resolverName) || NULL_RESOLVER_NAME.equals(resolverName) ? null : resolverName;
  }

  private ResolverInformation getResolverInformation(String resolverName) {
    ResolverInformation resolverInformation = null;
    String sanatizedName = sanitizeResolverName(resolverName);
    if (sanatizedName != null) {
      resolverInformation = new ResolverInformation(sanatizedName, requiresConnection, requiresConfiguration);
    }
    return resolverInformation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeResolversInformationModelProperty that = (TypeResolversInformationModelProperty) o;
    return Objects.equals(category, that.category) &&
        Objects.equals(inputResolvers, that.inputResolvers) &&
        Objects.equals(outputResolver, that.outputResolver) &&
        Objects.equals(keysResolver, that.keysResolver) &&
        Objects.equals(attributesResolver, that.attributesResolver) &&
        Objects.equals(partialTypeKeyResolver, that.partialTypeKeyResolver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, inputResolvers, outputResolver, keysResolver, attributesResolver, partialTypeKeyResolver);
  }

  @Override
  public String toString() {
    return "TypeResolversInformation{" +
        "category='" + category + '\'' +
        ", parameters=" + inputResolvers +
        ", outputResolver='" + outputResolver + '\'' +
        ", keysResolver='" + keysResolver + '\'' +
        ", attributesResolver='" + attributesResolver + '\'' +
        ", partialTypeKeyResolver='" + partialTypeKeyResolver + '\'' +
        '}';
  }
}
