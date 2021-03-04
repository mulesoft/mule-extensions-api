/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;

import java.util.Set;

/**
 * Immutable implementation of {@link OutputModel}
 *
 * @since 1.0
 */
public class ImmutableOutputModel extends AbstractImmutableModel implements OutputModel {

  private final MetadataType type;
  private final boolean hasDynamicType;

  /**
   * Creates a new instance with the given state
   *
   * @param description     the output's description
   * @param type            the output's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType  if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param modelProperties A {@link Set} of custom properties which extend this model
   */
  public ImmutableOutputModel(String description, MetadataType type, boolean hasDynamicType, Set<ModelProperty> modelProperties) {
    super(description, modelProperties);
    this.type = type;
    this.hasDynamicType = hasDynamicType;
  }

  @Override
  public MetadataType getType() {
    return type;
  }

  @Override
  public boolean hasDynamicType() {
    return hasDynamicType;
  }

}
