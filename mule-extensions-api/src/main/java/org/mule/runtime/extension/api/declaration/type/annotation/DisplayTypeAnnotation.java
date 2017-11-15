/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.api.meta.model.display.ClassValueModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TypeAnnotation} that contains information about the name and summary that should be rendered in the UI for a
 * particular model.
 * <p>
 * That information is obtained through the {@link DisplayName} and {@link Summary} annotations.
 *
 * @since 1.0
 */
public class DisplayTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "display";

  private final DisplayModel displayModel;

  /**
   * Creates a new instance
   *
   * @param displayModel a {@link DisplayModel}
   */
  public DisplayTypeAnnotation(DisplayModel displayModel) {
    this.displayModel = displayModel;
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
  public String getDisplayName() {
    return displayModel.getDisplayName();
  }

  /**
   * @return a brief overview about this model.
   */
  public String getSummary() {
    return displayModel.getSummary();
  }

  /**
   * @return a brief example about a possible value for this model.
   */
  public String getExample() {
    return displayModel.getExample();
  }

  /**
   * @return a {@link PathModel} instance if the carrier parameter points to a file or directory,
   * {@link Optional#empty()} otherwise.
   */
  public Optional<PathModel> getPathModel() {
    return displayModel.getPathModel();
  }

  public Optional<ClassValueModel> getClassValueModel() {
    return displayModel.getClassValueModel();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DisplayTypeAnnotation) {
      DisplayTypeAnnotation other = ((DisplayTypeAnnotation) obj);
      return Objects.equals(other.displayModel, this.displayModel);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return displayModel.hashCode();
  }
}
