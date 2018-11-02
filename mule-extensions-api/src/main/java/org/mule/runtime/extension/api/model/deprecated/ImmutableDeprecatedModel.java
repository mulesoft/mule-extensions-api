/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.deprecated;

import org.mule.runtime.api.meta.model.deprecated.DeprecatedModel;

/**
 * Immutable implementation of {@link DeprecatedModel}
 * 
 * @since 1.2
 */
public class ImmutableDeprecatedModel implements DeprecatedModel {

  private final String message;

  /**
   * {@inheritDoc}
   */
  public ImmutableDeprecatedModel(String message) {
    this.message = message;
  }


  @Override
  public String getMessage() {
    return message;
  }
}
