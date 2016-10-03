/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.internal.util.HierarchyClassMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * Base class for immutable implementations of a {@link Described} {@link EnrichableModel} model
 *
 * @since 1.0
 */
public abstract class AbstractImmutableModel implements Described, EnrichableModel {

  protected String description;
  protected final HierarchyClassMap<ModelProperty> modelProperties = new HierarchyClassMap<>();

  protected static void checkArgument(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Creates a new instance
   *
   * @param description     the model's description
   * @param modelProperties A {@link Set} of custom properties which extend this model
   */
  protected AbstractImmutableModel(String description, Set<ModelProperty> modelProperties) {
    this.description = description != null ? description : "";
    loadProperties(modelProperties);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getDescription() {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends ModelProperty> Optional<T> getModelProperty(Class<T> propertyType) {
    checkArgument(propertyType != null, "Cannot get model properties of a null type");
    return ofNullable((T) modelProperties.get(propertyType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ModelProperty> getModelProperties() {
    return unmodifiableSet(new HashSet(modelProperties.values()));
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }

  private void loadProperties(Collection<ModelProperty> properties) {
    if (properties != null) {
      properties.forEach(property -> modelProperties.put(property.getClass(), property));
    }
  }
}
