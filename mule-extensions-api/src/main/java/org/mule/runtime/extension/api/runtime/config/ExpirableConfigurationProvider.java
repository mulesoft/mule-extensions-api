/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;

import java.util.List;

/**
 * A specialization of the {@link ConfigurationProvider} interface which adds
 * the concept of expiration. The runtime will query the registered
 * {@link ConfigurationProvider configuration providers} which implement this interface
 * when it tries to locate expired configurations which need disposal
 *
 * @since 1.0
 * @deprecated since 1.4.0, the expirable configuration providers must handle expirations internally by
 * calling {@link ExtensionManager#disposeConfiguration(String key, ConfigurationInstance configuration)}.
 */
@NoImplement
@Deprecated
public interface ExpirableConfigurationProvider extends ConfigurationProvider {

  /**
   * @return a {@link List} which items are the expired {@link ConfigurationInstance}
   */
  List<ConfigurationInstance> getExpired();
}
