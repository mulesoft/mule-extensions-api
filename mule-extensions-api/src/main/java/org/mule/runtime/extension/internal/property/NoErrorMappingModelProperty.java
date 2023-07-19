/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Model property for indicating that the owning operation may not contain errorMappings.
 *
 * @since 1.4
 */
public class NoErrorMappingModelProperty implements ModelProperty {

  private static final long serialVersionUID = 1544876329052421254L;

  @Override
  public String getName() {
    return "noErrorMapping";
  }

  @Override
  public boolean isPublic() {
    return false;
  }

}
