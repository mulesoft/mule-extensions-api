/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.api.runtime.parameter.ParameterResolver;

/**
 * {@link TypeAnnotation} indicating that the real type of the of the annotated type is a {@link ParameterResolver}
 *
 * @since 1.0
 * @see ParameterResolver
 */
public class ParameterResolverTypeAnnotation implements TypeAnnotation {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "parameterResolver";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ParameterResolverTypeAnnotation;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
