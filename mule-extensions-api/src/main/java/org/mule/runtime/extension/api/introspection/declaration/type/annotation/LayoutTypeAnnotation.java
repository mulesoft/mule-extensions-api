/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.display.LayoutModel;

/**
 * Used to specify the alias name of the annotated {@link MetadataType}
 *
 * @since 1.0
 */
public class LayoutTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "layout";

  private final LayoutModel layoutModel;

  public LayoutTypeAnnotation(LayoutModel layoutModel) {
    this.layoutModel = layoutModel;
  }

  @Override
  public String getName() {
    return NAME;
  }

  public boolean isPassword() {
    return layoutModel.isPassword();
  }

  public boolean isText() {
    return layoutModel.isText();
  }

  public boolean isQuery() {
    return layoutModel.isQuery();
  }

  public int getOrder() {
    return layoutModel.getOrder();
  }

  public String getGroupName() {
    return layoutModel.getGroupName();
  }

  public String getTabName() {
    return layoutModel.getTabName();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof LayoutTypeAnnotation))
      return false;
    LayoutTypeAnnotation layoutTypeAnnotation = ((LayoutTypeAnnotation) obj);
    return layoutTypeAnnotation.isPassword() == this.isPassword() && layoutTypeAnnotation.isText() == this.isText()
        && layoutTypeAnnotation.isQuery() == this.isQuery() && layoutTypeAnnotation.getOrder() == this.getOrder()
        && layoutTypeAnnotation.getGroupName().equals(this.getGroupName())
        && layoutTypeAnnotation.getTabName().equals(this.getTabName());
  }
}
