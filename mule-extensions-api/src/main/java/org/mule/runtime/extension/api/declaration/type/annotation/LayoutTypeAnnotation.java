/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Text;

import java.util.Objects;

/**
 * A {@link TypeAnnotation} meant to be applied on {@link ObjectFieldType} instances and it contains information on how the field
 * should be rendered in the UI. That information is obtained through the {@link Text}, {@link Password} and {@link Placement}
 * annotations.
 *
 * @since 1.0
 */
public class LayoutTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "layout";

  private final LayoutModel layoutModel;

  /**
   * Creates a new instance
   *
   * @param layoutModel a {@link LayoutModel}
   */
  public LayoutTypeAnnotation(LayoutModel layoutModel) {
    this.layoutModel = layoutModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return Whether the underlying model represents a password and should be masked in the UI
   */
  public boolean isPassword() {
    return layoutModel.isPassword();
  }

  /**
   * @return Whether the underlying model should be edited with a multi line string editor in the UI
   */
  public boolean isText() {
    return layoutModel.isText();
  }

  /**
   * @return Whether the underlying model represents a query and should be treated accordingly in the UI
   */
  public boolean isQuery() {
    return layoutModel.isQuery();
  }

  /**
   * @return The order of the model within its group.
   */
  public int getOrder() {
    return layoutModel.getOrder();
  }

  /**
   * @return The group element name where the model is going to be located.
   */
  public String getGroupName() {
    return layoutModel.getGroupName();
  }

  /**
   * @return The tab element name where the model and its group it's going to be located.
   */
  public String getTabName() {
    return layoutModel.getTabName();
  }

  /**
   * @return The underlying {@link LayoutModel} that this {@link TypeAnnotation} represents.
   */
  public LayoutModel getLayoutModel() {
    return layoutModel;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof LayoutTypeAnnotation) {
      LayoutTypeAnnotation other = ((LayoutTypeAnnotation) obj);
      return Objects.equals(other.getLayoutModel(), this.getLayoutModel());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return layoutModel.hashCode();
  }
}
