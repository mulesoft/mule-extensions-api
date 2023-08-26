/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Model property that indicates that the enriched model should not be used in an implicit way
 *
 * @since 1.3.0
 */
public class NoImplicitModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "NoImplicit";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
