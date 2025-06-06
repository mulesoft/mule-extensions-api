/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
