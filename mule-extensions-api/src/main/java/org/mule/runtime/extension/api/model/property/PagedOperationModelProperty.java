/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.property;

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
  public boolean isExternalizable() {
    return false;
  }
}
