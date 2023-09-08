/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * A {@link ModelProperty} which indicates that the enriched element should not be a part of any generated Connectivity Schema
 *
 * @since 1.5.0
 */
public class ExcludeFromConnectivitySchemaModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "ExcludeFromConnectivitySchema";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
