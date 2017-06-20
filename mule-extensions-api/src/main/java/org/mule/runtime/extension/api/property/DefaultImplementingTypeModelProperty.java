/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

/**
 * Declares the default {@code Type} that will be instantiated for this {@link ParameterModel}
 * if no instance is provided as a value.
 *
 * @since 1.0
 */
public class DefaultImplementingTypeModelProperty implements ModelProperty {

  public static final String NAME = "defaultType";

  private final MetadataType defaultType;

  public DefaultImplementingTypeModelProperty(MetadataType defaultType) {
    this.defaultType = defaultType;
  }

  /**
   * @return the default {@link MetadataType} to be used when creating the default instance for
   * the associated {@link ParameterModel}
   */
  public MetadataType value() {
    return defaultType;
  }

  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return true
   */
  @Override
  public boolean isPublic() {
    return true;
  }
}
