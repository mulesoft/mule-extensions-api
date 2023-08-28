/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals;

import java.util.Set;

/**
 * * A {@link TypeAnnotation} used to enrich an {@link ObjectType} by specifying that the contained parameters have an exclusive
 * relation as it is specified in {@link ExclusiveOptionals}
 * <p>
 * This class is immutable.
 *
 * @since 1.0
 */
public class ExclusiveOptionalsTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "exclusiveOptionals";

  private final Set<String> exclusiveParameterNames;
  private final boolean isOneRequired;

  public ExclusiveOptionalsTypeAnnotation(Set<String> exclusiveParameterNames, boolean isOneRequired) {
    this.exclusiveParameterNames = exclusiveParameterNames;
    this.isOneRequired = isOneRequired;
  }

  public Set<String> getExclusiveParameterNames() {
    return exclusiveParameterNames;
  }

  public boolean isOneRequired() {
    return isOneRequired;
  }

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
    return obj instanceof ExclusiveOptionalsTypeAnnotation;
  }

  @Override
  public boolean isPublic() {
    return true;
  }
}
