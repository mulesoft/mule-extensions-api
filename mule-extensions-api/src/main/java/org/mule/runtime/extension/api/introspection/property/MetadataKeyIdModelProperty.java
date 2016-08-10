/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;


/**
 * A {@link ModelProperty} for a of {@link SourceModel} and {@link OperationModel} parameters
 * that indicates that its a {@link MetadataKeyId}.
 *
 * @since 1.0
 */
public final class MetadataKeyIdModelProperty implements ModelProperty {

  private final MetadataType type;

  /**
   * Creates a new instance.
   *
   * @param type of the {@link ParameterModel} annotated with {@link MetadataKeyId}.
   */
  public MetadataKeyIdModelProperty(MetadataType type) {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "MetadataKeyId";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExternalizable() {
    return true;
  }

  /**
   * @return the type of the {@link ParameterModel} annotated with {@link MetadataKeyId}
   */
  public MetadataType getType() {
    return type;
  }
}
