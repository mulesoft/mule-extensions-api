/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
