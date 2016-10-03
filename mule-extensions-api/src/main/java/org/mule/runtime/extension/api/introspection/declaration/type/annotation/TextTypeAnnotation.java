/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.extension.api.annotation.param.display.Text;

/**
 * A marker {@link TypeAnnotation} meant to be applied on
 * {@link ObjectFieldType} instances which {@link ObjectFieldType#getValue()}
 * points to an {@link StringType}. The presence of this annotation means
 * that such String should be interpreted as {@link Text}
 *
 * @since 1.0
 */
public class TextTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "text";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int hashCode() {
    return NAME.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TextTypeAnnotation;
  }

}
