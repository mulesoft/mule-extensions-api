/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.annotation.Query;
import org.mule.runtime.extension.api.introspection.ModelProperty;

/**
 * Marker {@link ModelProperty} that indicates that an operation is a {@link Query} operation.
 *
 * @since 1.0
 */
public class QueryOperationModelProperty implements ModelProperty {

  /**
   * @return {@code queryOperation}
   */
  @Override
  public String getName() {
    return "queryOperation";
  }

  /**
   * @return {@code true}
   */
  @Override
  public boolean isExternalizable() {
    return true;
  }
}
