/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} for indicating that the owning operation may not have a configuring streaming strategy, regardless of it
 * being a streaming operation.
 *
 * @since 1.5
 */
public class NoStreamingConfigurationModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "noStreamingConfiguration";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
