/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.MarkerAnnotation;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectType;

/**
 * A {@link TypeAnnotation} used as a marker for {@link ObjectType} instances, signaling that such type is an Infrastructure Type.
 *
 * @since 1.0
 */
public class InfrastructureTypeAnnotation extends MarkerAnnotation {

  public static final String NAME = "infrastructureType";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String toString() {
    return getName();
  }
}
