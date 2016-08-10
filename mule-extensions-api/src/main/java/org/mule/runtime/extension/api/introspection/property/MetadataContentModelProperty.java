/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

/**
 * A {@link ModelProperty} for {@link ParameterModel} of Sources and Operations parameters
 * that indicates if a parameter is considered as the main input of the operation
 *
 * @since 1.0
 */
public final class MetadataContentModelProperty implements ModelProperty {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "MetadataContent";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExternalizable() {
    return true;
  }
}
