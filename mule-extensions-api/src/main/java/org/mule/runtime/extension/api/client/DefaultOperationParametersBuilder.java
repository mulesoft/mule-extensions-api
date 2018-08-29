/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.extension.internal.client.ComplexParameter;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mule.runtime.internal.dsl.DslConstants.CONFIG_ATTRIBUTE_NAME;

/**
 * Builder pattern implementation for building a new {@link OperationParameters} instance.
 *
 * @since 1.0
 */
public final class DefaultOperationParametersBuilder {

  private String configRef;
  private Map<String, Object> parameters = new LinkedHashMap<>();

  DefaultOperationParametersBuilder() {}

  /**
   * Sets the name of the config used to execute an operation.
   */
  public DefaultOperationParametersBuilder configName(String configRef) {
    this.configRef = configRef;
    addParameter(CONFIG_ATTRIBUTE_NAME, configRef);
    return this;
  }

  /**
   * Ads a new parameter to execute the operation with.
   */
  public DefaultOperationParametersBuilder addParameter(String name, Object value) {
    this.parameters.put(name, value);
    return this;
  }

  /**
   * Ads a new complex parameter to execute the operation with. The {@code type} will be instantiated and all the provided
   * {@code params} will be set to that new instance.
   */
  public DefaultOperationParametersBuilder addParameter(String name, Class<?> type, DefaultOperationParametersBuilder params) {
    this.parameters.put(name, new ComplexParameter(type, params.build().get()));
    return this;
  }

  /**
   * Builds a new {@link OperationParameters} instance with all the configured parameters.
   */
  public DefaultOperationParameters build() {
    return new DefaultOperationParameters(configRef, parameters);
  }
}
