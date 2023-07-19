/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;

import java.util.Optional;

/**
 * Represents a component which might be associated to a {@link ConfigurationInstance}
 *
 * @since 1.0
 */
@NoImplement
public interface ConfiguredComponent {

  /**
   * @return The {@link ConfigurationInstance} which may optionally be associated to {@code this} component
   */
  Optional<ConfigurationInstance> getConfigurationInstance();
}
