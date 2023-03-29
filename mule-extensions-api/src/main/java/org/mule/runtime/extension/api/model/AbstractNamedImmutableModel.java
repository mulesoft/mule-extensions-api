/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.HasDisplayModel;

import java.util.Optional;
import java.util.Set;


/**
 * Base class for immutable implementations of {@link NamedObject} introspection models
 *
 * @since 1.0
 */
public abstract class AbstractNamedImmutableModel extends AbstractImmutableModel implements NamedObject, HasDisplayModel {

  private final String name;
  private final DisplayModel displayModel;

  /**
   * Creates a new instance
   *
   * @param name            the model's name
   * @param description     the model's description
   * @param displayModel    a model containing directives about how this component is to be displayed in the UI
   * @param modelProperties A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractNamedImmutableModel(String name, String description, DisplayModel displayModel,
                                        Set<ModelProperty> modelProperties) {
    super(description, modelProperties);

    checkArgument(name != null && name.length() > 0, "Name attribute cannot be null or blank");
    this.name = name;
    this.displayModel = displayModel;
  }

  protected static void checkArgument(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<DisplayModel> getDisplayModel() {
    return ofNullable(displayModel);
  }

  /**
   * Defines object equality based on the given object being an object of this class and in the equality of the {@link #getName()}
   * attributes
   *
   * @param obj an object
   * @return {@code true} if equal
   */
  @Override
  public boolean equals(Object obj) {
    return getClass().isInstance(obj) && name.equals(((NamedObject) obj).getName());

  }

  /**
   * Calculates hashcode based on {@link #getName()}
   *
   * @return a hash code
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "AbstractNamedImmutableModel{" +
        "name='" + name + '\'' +
        ", displayModel=" + displayModel +
        "} " + super.toString();
  }
}
