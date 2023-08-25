/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.client.params;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Parameterizes a generic entity executed or created through the {@link ExtensionsClient}
 * <p>
 * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
 *
 * @since 1.5.0
 */
@Experimental
@MinMuleVersion("4.5.0")
public interface Parameterizer<T extends Parameterizer> {

  /**
   * Sets a parameter with a given value, automatically determining the group the parameter belongs to.
   *
   * @param parameterName the name of the parameter within the {@code paramGroupName} group to set.
   * @param value         the value of the parameter to set
   * @return {@code this} instance
   */
  T withParameter(String parameterName, Object value);

  /**
   * Sets a parameter with a given value.
   *
   * @param parameterGroup the name of the group containing the parameter to set.
   * @param parameterName  the name of the parameter within the {@code paramGroupName} group to set.
   * @param value          the value of the parameter to set
   * @return {@code this} instance
   */
  T withParameter(String parameterGroup, String parameterName, Object value);

}
