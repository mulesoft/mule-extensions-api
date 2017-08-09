/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.annotation.TypeAnnotation;

/**
 * Contains directives regarding syntax and semantics of the generated XML DSL
 *
 * @since 1.0
 */
public class ParameterDslAnnotation implements TypeAnnotation {

  public static final String NAME = "parameterDsl";

  private final boolean allowInlineDefinition;
  private final boolean allowReferences;


  /**
   * Creates a new instance
   *
   * @param allowInlineDefinition   whether the associated element should support inline definition as child element
   * @param allowReferences         whether the associated element should support registry references
   */
  public ParameterDslAnnotation(boolean allowInlineDefinition, boolean allowReferences) {
    this.allowInlineDefinition = allowInlineDefinition;
    this.allowReferences = allowReferences;
  }

  /**
   * @return {@code xmlHints}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return whether the associated element should support inline definition as child element
   */
  public boolean allowsInlineDefinition() {
    return allowInlineDefinition;
  }

  /**
   * @return whether the associated element should support registry references
   */
  public boolean allowsReferences() {
    return allowReferences;
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ParameterDslAnnotation) {
      ParameterDslAnnotation other = (ParameterDslAnnotation) obj;
      return allowInlineDefinition == other.allowsInlineDefinition() &&
          allowReferences == other.allowsReferences();
    }

    return false;
  }
}
