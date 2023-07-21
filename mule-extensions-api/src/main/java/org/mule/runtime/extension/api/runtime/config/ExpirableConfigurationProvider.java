/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;

import java.util.List;

/**
 * A specialization of the {@link ConfigurationProvider} interface which adds the concept of expiration. The runtime will query
 * the registered {@link ConfigurationProvider configuration providers} which implement this interface when it tries to locate
 * expired configurations which need disposal
 *
 * @since 1.0
 */
@NoImplement
public interface ExpirableConfigurationProvider extends ConfigurationProvider {

  /**
   * @return a {@link List} which items are the expired {@link ConfigurationInstance}
   */
  List<ConfigurationInstance> getExpired();
}
