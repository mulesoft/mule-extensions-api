/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.operation.OperationModel;

/**
 * A marker {@link ModelProperty} to signal that the owning {@link OperationModel} is paged.
 *
 * @since 1.0
 */
public class PagedOperationModelProperty implements ModelProperty {

  /**
   * @return {@code paged}
   */
  @Override
  public String getName() {
    return "paged";
  }

  /**
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
