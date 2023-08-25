/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;

/**
 * Marker annotation for a field that is a {@link ConfigOverride}.
 *
 * @since 1.0
 */
public class ConfigOverrideTypeAnnotation implements TypeAnnotation {

  /**
   * Creates a new instance
   */
  public ConfigOverrideTypeAnnotation() {}

  @Override
  public String getName() {
    return "configOverride";
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
