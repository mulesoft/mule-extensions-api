/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

import java.util.Objects;

/**
 * Marker annotation for a {@link ObjectFieldType} that represents a {@link ParameterModel#isComponentId() componentId}.
 *
 * @since 1.2.0
 */
public class ComponentIdFieldTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "isComponentId";

  /**
   * Creates a new instance
   */
  public ComponentIdFieldTypeAnnotation() {}

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int hashCode() {
    return NAME.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && this.getClass().equals(obj.getClass());
  }
}
