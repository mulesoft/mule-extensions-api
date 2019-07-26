/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
