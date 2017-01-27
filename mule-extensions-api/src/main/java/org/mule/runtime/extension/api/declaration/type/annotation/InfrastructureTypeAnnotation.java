/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.MarkerAnnotation;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectType;

/**
 * A {@link TypeAnnotation} used as a marker for {@link ObjectType} instances, signaling that such type is an Infrastructure Type.
 *
 * @since 1.0
 */
public class InfrastructureTypeAnnotation extends MarkerAnnotation {

  public static final String NAME = "infrastructureType";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
