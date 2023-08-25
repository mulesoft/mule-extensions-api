/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;

/**
 * A marker {@link TypeAnnotation} meant to be applied on {@link ObjectFieldType} instances which
 * {@link ObjectFieldType#getValue()} points to an {@link ObjectType}. The presence of this annotation means that the fields in
 * such target object should be flattened into the owner {@link ObjectType}.
 *
 * @since 1.0
 */
public class FlattenedTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "flattened";

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
    return obj instanceof FlattenedTypeAnnotation;
  }
}
