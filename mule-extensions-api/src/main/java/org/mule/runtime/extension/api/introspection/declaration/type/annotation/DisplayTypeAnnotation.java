/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.display.DisplayModel;

/**
 * Used to specify the alias name of the annotated {@link MetadataType}
 *
 * @since 1.0
 */
public class DisplayTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "display";

  private final DisplayModel displayModel;

  public DisplayTypeAnnotation(DisplayModel displayModel) {
    this.displayModel = displayModel;
  }

  @Override
  public String getName() {
    return NAME;
  }

  public String getDisplayName() {
    return displayModel.getDisplayName();
  }

  public String getSummary() {
    return displayModel.getSummary();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DisplayTypeAnnotation))
      return false;
    DisplayTypeAnnotation displayTypeAnnotation = ((DisplayTypeAnnotation) obj);
    return displayTypeAnnotation.getDisplayName().equals(this.getDisplayName())
        && displayTypeAnnotation.getSummary().equals(this.getSummary());
  }
}
