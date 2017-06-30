/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.api.meta.model.display.PathModel;

/**
 * Carries metadata of a parameter that points to a file or directory
 *
 * @since 1.0
 */
public class PathInformation implements TypeAnnotation {

  public static final String NAME = "path";
  private final PathModel pathModel;

  PathInformation(PathModel pathModel) {
    this.pathModel = pathModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return the name which should be use to render this model.
   */
  public boolean isDirectory() {
    return pathModel.isDirectory();
  }

  @Override
  public boolean equals(Object obj) {
    return reflectionEquals(obj, this);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }
}
