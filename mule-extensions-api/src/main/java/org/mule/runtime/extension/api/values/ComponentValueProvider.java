/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.values;

import static java.util.Collections.emptyList;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.parameter.ValueProviderModel;
import org.mule.runtime.api.value.Value;

import java.util.List;
import java.util.Set;

/**
 * This interface allows the exposure of the {@link Value values} associated to a parameter of a Component.
 *
 * @since 1.0
 */
@NoImplement
public interface ComponentValueProvider {

  /**
   * @param providerName the name of the parameter for which resolve their possible {@link Value values}
   * @return the resolved {@link Value values}
   */
  Set<Value> getValues(String providerName) throws ValueResolvingException;

  /**
   * Retrieves the list of associated {@link ValueProviderModel} with the given provider name in the component
   *
   * @param providerName The name of the value provider
   * @return The associated {@link ValueProviderModel}
   */
  @Deprecated
  default List<ValueProviderModel> getModels(String providerName) {
    return emptyList();
  }

}
