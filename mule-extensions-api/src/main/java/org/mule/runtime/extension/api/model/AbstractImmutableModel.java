/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.ofNullable;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.ImmutableSet.of;

import org.mule.runtime.api.meta.DescribedObject;
import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ModelProperty;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;


/**
 * Base class for immutable implementations of a {@link DescribedObject} {@link EnrichableModel} model
 *
 * @since 1.0
 */
public abstract class AbstractImmutableModel implements DescribedObject, EnrichableModel {

  protected String description;
  protected final Map<Class<? extends ModelProperty>, ModelProperty> modelProperties = new LinkedHashMap<>();

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
    return unmodifiableSet(new LinkedHashSet<>(modelProperties.values()));
  }

  @Override
  public String toString() {
    return "AbstractImmutableModel{" +
        "description='" + description + '\'' +
        ", modelProperties=" + modelProperties +
        '}';
  }

  protected <T> Set<T> copy(Set<T> values) {
    return values != null ? copyOf(values) : of();
  }

  protected <T> List<T> copy(List<T> values) {
    return values != null ? ImmutableList.copyOf(values) : ImmutableList.of();
  }

  private void loadProperties(Collection<ModelProperty> properties) {
    if (properties != null) {
      properties.forEach(property -> modelProperties.put(property.getClass(), property));
    }
  }
}
