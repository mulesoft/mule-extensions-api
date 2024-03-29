/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * Marks that the annotated type is of {@link Extensible} kind, declaring that a given type can be extended by others in the
 * context of the {@link ExtensionModel}.
 *
 * @since 1.0
 */
public class ExtensibleTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "extensibleType";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public int hashCode() {
    return NAME.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ExtensibleTypeAnnotation;
  }
}
