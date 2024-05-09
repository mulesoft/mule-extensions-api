/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Marker {@link ModelProperty} for modules to avoid propagating connectivity errors from operations.
 *
 * @since 1.8
 */
public class NoConnectivityErrorModelProperty implements ModelProperty {

  private static final long serialVersionUID = 1L;

  @Override
  public String getName() {
    return "noConnectivityError";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
