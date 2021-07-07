/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Marker {@link ModelProperty} for extension walker to skip the component when walking the AST.
 *
 * @since 1.0
 */
public class SkipOnWalkModelProperty implements ModelProperty {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "skipOnWalk";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
