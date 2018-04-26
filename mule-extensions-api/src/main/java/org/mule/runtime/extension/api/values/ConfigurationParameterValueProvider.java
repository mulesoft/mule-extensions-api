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
 * This interface allows the exposure of the {@link Set} of {@link Value values} associated to a Configuration's or
 * Connection Provider's parameter.
 *
 * @since 1.0
 */
@NoImplement
public interface ConfigurationParameterValueProvider {

  /**
   * Resolves the possible {@link Value values} for the Configuration's parameter identified by the {@code parameterName}
   *
   * @param parameterName the name of the parameter that has the capability to provide {@link Value values}
   * @return {@link Set} of possible and valid {@link Value values}
   */
  Set<Value> getConfigValues(String parameterName) throws ValueResolvingException;

  /**
   * Retrieves the list of associated {@link ValueProviderModel} with the given provider name in the configuration
   *
   * @param providerName The name of the value provider
   * @return The associated {@link ValueProviderModel}
   */
  @Deprecated
  default List<ValueProviderModel> getConfigModels(String providerName) throws ValueResolvingException {
    return emptyList();
  }

  /**
   * Resolves the possible {@link Value values} for the Connection Providers's parameter identified by the {@code parameterName}
   *
   * @param parameterName the name of the parameter that has the capability to provide {@link Value values}
   * @return {@link Set} of possible and valid {@link Value values}
   */
  Set<Value> getConnectionValues(String parameterName) throws ValueResolvingException;

  /**
   * Retrieves the list of associated {@link ValueProviderModel} with the given provider name in the connection
   *
   * @param providerName The name of the value provider
   * @return The associated {@link ValueProviderModel}
   */
  @Deprecated
  default List<ValueProviderModel> getConnectionModels(String providerName) throws ValueResolvingException {
    return emptyList();
  }

}
