/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;

/**
 * A {@link TypeAnnotation} used to enrich a {@link MetadataType} by specifying a certain level of {@link ExpressionSupport} it
 * supports.
 * <p>
 * This class is immutable.
 *
 * @since 1.0
 */
public final class ExpressionSupportAnnotation implements TypeAnnotation {

  public static final String NAME = "expressionSupport";
  private final ExpressionSupport value;

  /**
   * Creates a new instance
   *
   * @param expressionSupport a {@link ExpressionSupport}
   * @throws IllegalArgumentException if {@code expressionSupport} is {@code null}
   */
  public ExpressionSupportAnnotation(ExpressionSupport expressionSupport) {
    if (expressionSupport == null) {
      throw new IllegalArgumentException("expressionSupport cannot be null");
    }
    this.value = expressionSupport;
  }

  public ExpressionSupport getExpressionSupport() {
    return value;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ExpressionSupportAnnotation) {
      return value == ((ExpressionSupportAnnotation) obj).getExpressionSupport();
    }

    return false;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value.name();
  }
}
