/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.api.meta.model.parameter.ElementReference;

/**
 * A public {@link TypeAnnotation} intended to be used on {@link ObjectFieldType} types in order to
 * communicate an associated {@link ElementReference}
 *
 * @since 1.0
 */
public class ElementReferenceTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "elementReference";
  private final ElementReference elementReference;

  /**
   * Creates a new instance
   *
   * @param elementReference the reference to be communicated
   */
  public ElementReferenceTypeAnnotation(ElementReference elementReference) {
    this.elementReference = elementReference;
  }

  /**
   * @return An {@link ElementReference}
   */
  public ElementReference getElementReference() {
    return elementReference;
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
}
