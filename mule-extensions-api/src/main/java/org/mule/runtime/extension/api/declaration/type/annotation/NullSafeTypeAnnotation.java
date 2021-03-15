/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.extension.api.annotation.param.NullSafe;

/**
 * A {@link TypeAnnotation} meant to be applied on {@link ObjectFieldType} instances. It marks such field as {@link NullSafe} and
 * contains the {@link #type} of the value which should be created if such field is not provided
 *
 * @since 1.0
 */
public class NullSafeTypeAnnotation implements TypeAnnotation {

  private final Class<?> type;
  private boolean defaultOverride;

  /**
   * Creates a new instance
   * 
   * @param type            the type of the default value to be created
   * @param defaultOverride whether the default implementing class has been overriden or not
   */
  public NullSafeTypeAnnotation(Class<?> type, boolean defaultOverride) {
    this.type = type;
    this.defaultOverride = defaultOverride;
  }

  public Class<?> getType() {
    return type;
  }

  public boolean hasDefaultOverride() {
    return defaultOverride;
  }

  @Override
  public String getName() {
    return "nullSafe";
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
