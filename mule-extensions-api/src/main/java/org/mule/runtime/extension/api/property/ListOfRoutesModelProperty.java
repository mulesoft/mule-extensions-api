/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;

/**
 * Indicates that the {@link NestedRouteModel} with this property may be present more than once in its component.
 *
 * @since 1.10, 1.9.7
 */
public class ListOfRoutesModelProperty implements ModelProperty {

  private static final long serialVersionUID = 1L;

  public static final String NAME = "listOfRoutes";

  /**
   * Use this instance for memory efficiency
   */
  public static final ListOfRoutesModelProperty INSTANCE = new ListOfRoutesModelProperty();

  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return {@code true} since DSL must be handled consistently even after a serialization/deserialization of the extension
   *         model.
   */
  @Override
  public boolean isPublic() {
    return true;
  }

}
