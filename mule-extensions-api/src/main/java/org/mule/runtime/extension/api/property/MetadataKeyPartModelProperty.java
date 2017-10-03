/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;


/**
 * A {@link ModelProperty} for {@link ParameterModel} of {@link SourceModel} and {@link OperationModel}
 * parameters that indicates that the is a part of a {@link MetadataKey}.
 *
 * @since 1.0
 */
public final class MetadataKeyPartModelProperty implements ModelProperty {

  private final int order;

  /**
   * Creates a new instance.
   *
   * @param order the order of the parameter in the {@link MetadataKey};
   */
  public MetadataKeyPartModelProperty(int order) {
    this.order = order;
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
  public boolean isPublic() {
    return true;
  }

  /**
   * The order of this parameter in the {@link MetadataKey}.
   *
   * @return the order of the parameter for a composed {@link MetadataKey}, 0 if is a simple {@link MetadataKey}
   */
  public int getOrder() {
    return order;
  }
}
