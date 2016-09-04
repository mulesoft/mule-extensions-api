/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.annotation.TypeAnnotation;

/**
 * Marks that the annotated type should not be declared as a global type
 * when generating the DSL support
 *
 * @since 1.0
 */
public class NoGlobalTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "notGlobalType";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof NoGlobalTypeAnnotation;
  }
}
