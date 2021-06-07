/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;

import java.util.Comparator;

/**
 * A {@link Comparator} for sorting objects of type {@link ObjectFieldType} based on its layout annotation.
 */
public class LayoutOrderComparator implements Comparator<ObjectFieldType> {

  public static final LayoutOrderComparator BY_LAYOUT_ORDER = new LayoutOrderComparator();

  private LayoutOrderComparator() {
    // Nothing to do
  }

  @Override
  public int compare(ObjectFieldType o1, ObjectFieldType o2) {
    return o1.getAnnotation(LayoutTypeAnnotation.class).flatMap(LayoutTypeAnnotation::getOrder).orElse(-1)
        - o2.getAnnotation(LayoutTypeAnnotation.class).flatMap(LayoutTypeAnnotation::getOrder).orElse(-1);
  }

}
