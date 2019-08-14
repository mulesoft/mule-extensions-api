/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
