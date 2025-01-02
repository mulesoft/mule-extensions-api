/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Model property that indicates that the enriched model cannot be used with an implicit configs, since the actual config to use
 * cannot be inferred.
 *
 * @since 1.10
 */
public class ManyImplicitConfigsModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "ManyImplicitConfigs";
  }

  @Override
  public boolean isPublic() {
    return false;
  }

}
