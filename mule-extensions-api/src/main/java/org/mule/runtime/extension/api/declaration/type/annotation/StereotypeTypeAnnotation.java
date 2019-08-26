/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A public {@link TypeAnnotation} intended to be used on {@link ObjectFieldType} types in order to
 * communicate an associated {@link StereotypeModel}
 *
 * @since 1.0
 */
public class StereotypeTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "stereotype";
  private final List<StereotypeModel> allowedStereotypes;
  private transient List<Class<? extends StereotypeDefinition>> definitionClasses;

  /**
   * Creates a new instance which only holds a reference to the {@code definitionClasses}.
   *
   * Those classes are to later be resolved into {@link StereotypeModel} instances through an invocation to the
   * {@link #resolveStereotypes(Function)} method
   *
   * @param definitionClasses stereotype definitions
   */
  public static StereotypeTypeAnnotation fromDefinitions(List<Class<? extends StereotypeDefinition>> definitionClasses) {
    StereotypeTypeAnnotation annotation = new StereotypeTypeAnnotation(new ArrayList<>(definitionClasses.size()));
    annotation.definitionClasses = definitionClasses;

    return annotation;
  }

  /**
   * Creates a new instance
   *
   * @param allowedStereotypes the stereotypes models
   */
  public StereotypeTypeAnnotation(List<StereotypeModel> allowedStereotypes) {
    this.allowedStereotypes = allowedStereotypes;
  }

  /**
   * @return The allowed {@link StereotypeModel stereotypes}
   */
  public List<StereotypeModel> getAllowedStereotypes() {
    return allowedStereotypes;
  }

  public void resolveStereotypes(Function<Class<? extends StereotypeDefinition>, StereotypeModel> resolver) {
    if (allowedStereotypes.isEmpty()) {
      definitionClasses.forEach(clazz -> allowedStereotypes.add(resolver.apply(clazz)));
    }
  }

  public void resolveStereotypes(ObjectType objectType,
                                 BiFunction<ObjectType, Class<? extends StereotypeDefinition>, StereotypeModel> resolver) {
    if (allowedStereotypes.isEmpty()) {
      definitionClasses.forEach(clazz -> allowedStereotypes.add(resolver.apply(objectType, clazz)));
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return {@link #NAME}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  @Override
  public int hashCode() {
    return allowedStereotypes.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StereotypeTypeAnnotation other = (StereotypeTypeAnnotation) obj;
    if (allowedStereotypes == null) {
      if (other.allowedStereotypes != null) {
        return false;
      }
    } else if (!allowedStereotypes.equals(other.allowedStereotypes)) {
      return false;
    }
    return true;
  }

}
