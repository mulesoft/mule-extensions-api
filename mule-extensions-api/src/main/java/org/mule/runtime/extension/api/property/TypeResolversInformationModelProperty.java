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

  public static final String NAME = "TypeResolversInformation";
  private final String category;
  private final Map<String, String> inputResolvers;
  private final String outputResolver;
  private final String attributesResolver;

  public TypeResolversInformationModelProperty(String category,
                                               Map<String, String> parameters,
                                               String outputResolver, String attributesResolver) {
    checkArgument(isNotBlank(category), "A Category name is required for a group of resolvers");
    this.category = category;
    this.inputResolvers = parameters != null && parameters.isEmpty() ? null : parameters;
    this.outputResolver = sanitizeResolverName(outputResolver);
    this.attributesResolver = sanitizeResolverName(attributesResolver);
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
   * Provides the name of the resolver (if any) associated to a given parameter.
   *
   * @param parameterName name of the parameter
   * @return Name of the resolver associated to the parameter, if one exists.
   */
  public Optional<String> getParameterResolverName(String parameterName) {
    return inputResolvers == null ? empty() : ofNullable(inputResolvers.get(parameterName));
  }

  /**
   * Provides the name of the output resolver (if any) associated to the Component.
   *
   * @return output resolver's name
   */
  public Optional<String> getOutputResolverName() {
    return ofNullable(outputResolver);
  }

  /**
   * Provides the name of the output attributes resolver (if any) associated to the Component.
   *
   * @return output resolver's name
   */
  public Optional<String> getAttributesResolverName() {
    return ofNullable(attributesResolver);
  }

  private String sanitizeResolverName(String resolverName) {
    return isAllBlank(resolverName) || NULL_RESOLVER_NAME.equals(resolverName) ? null : resolverName;
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
        Objects.equals(attributesResolver, that.attributesResolver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, inputResolvers, outputResolver, attributesResolver);
  }

  @Override
  public String toString() {
    return "TypeResolversInformation{" +
        "category='" + category + '\'' +
        ", parameters=" + inputResolvers +
        ", outputResolver='" + outputResolver + '\'' +
        ", attributesResolver='" + attributesResolver + '\'' +
        '}';
  }
}
