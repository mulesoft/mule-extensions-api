/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.api.metadata.TypedValue;

/**
 * {@link TypeAnnotation} indicating that the real type of the of the annotated type is a {@link TypedValue}
 *
 * @since 1.0
 * @see TypedValue
 */
public final class TypedValueTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "typedValue";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypedValueTypeAnnotation;
  }

  @Override
  public int hashCode() {
    return TypedValueTypeAnnotation.class.hashCode();
  }

  /**
   * @return false
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
