/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.values;

import static java.util.Collections.emptyList;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.parameter.ValueProviderModel;
import org.mule.runtime.api.value.Value;
import org.mule.sdk.api.annotation.values.FieldValues;

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
   * Gets the values for the parameter of the given {@code parameterName}, using the given {@code targetSelector}. See
   * {@link FieldValues#targetSelectors}
   *
   * @param parameterName  the name of the parameter for which its {@link Value values} will be resolved
   * @param targetSelector the target selector to use to resolve the {@link Value values}
   * @return the resolved {@link Value values}
   * @throws ValueResolvingException if there was an error resolving the {@link Value values}
   *
   * @since 1.4
   */
  Set<Value> getValues(String parameterName, String targetSelector) throws ValueResolvingException;

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
