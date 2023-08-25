/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
