/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static org.mule.runtime.api.util.Preconditions.checkArgument;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;

import java.util.Objects;

/**
 * Declares the default {@code Type} that will be instantiated for this {@link ObjectFieldType} if no instance is provided as a
 * value.
 *
 * @since 1.0
 */
public class DefaultImplementingTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "defaultImplementingType";

  private final MetadataType defaultType;

  public DefaultImplementingTypeAnnotation(MetadataType defaultType) {
    checkArgument(defaultType != null, "Default implementing type cannot be null");
    this.defaultType = defaultType;
  }

  /**
   * @return the default {@link MetadataType} to be used when creating the default instance for the associated
   *         {@link ObjectFieldType}
   */
  public MetadataType getDefaultType() {
    return defaultType;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean isPublic() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DefaultImplementingTypeAnnotation) {
      return Objects.equals(this.defaultType, ((DefaultImplementingTypeAnnotation) o).defaultType);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return defaultType.hashCode();
  }
}
