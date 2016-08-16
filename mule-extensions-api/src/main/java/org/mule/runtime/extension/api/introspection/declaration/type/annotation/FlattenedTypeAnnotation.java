/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;

/**
 * A marker {@link TypeAnnotation} meant to be applied on
 * {@link ObjectFieldType} instances which {@link ObjectFieldType#getValue()}
 * points to an {@link ObjectType}. The presence of this annotation means
 * that the fields in such target object should be flattened into the
 * owner {@link ObjectType}.
 *
 * @since 1.0
 */
public class FlattenedTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "flattened";

  @Override
  public String getName() {
    return NAME;
  }
}
