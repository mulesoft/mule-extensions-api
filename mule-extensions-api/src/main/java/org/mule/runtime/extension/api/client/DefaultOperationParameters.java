/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import static java.util.Optional.ofNullable;

import static org.mule.runtime.internal.dsl.DslConstants.CONFIG_ATTRIBUTE_NAME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

/**
 * Default {@link OperationParameters} implementation, works as a generic {@link OperationParameters} for every extension
 * operation.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public class DefaultOperationParameters implements OperationParameters {

  private final Map<String, Object> parameters;

  DefaultOperationParameters(Map<String, Object> parameters) {
    this.parameters = ImmutableMap.copyOf(parameters);
  }

  /**
   * Deprecated constructor, the config-ref param is not deleted to avoid breaking API, but is now bundled in the parameters
   */
  @Deprecated
  DefaultOperationParameters(String configRef, Map<String, Object> parameters) {
    this.parameters = ImmutableMap.copyOf(parameters);
  }

  /**
   * @return a new build {@link DefaultOperationParametersBuilder} instance to create a {@link OperationParameters} instance.
   */
  public static DefaultOperationParametersBuilder builder() {
    return new DefaultOperationParametersBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<String> getConfigName() {
    return ofNullable(((String) parameters.get(CONFIG_ATTRIBUTE_NAME)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> get() {
    return parameters;
  }
}
