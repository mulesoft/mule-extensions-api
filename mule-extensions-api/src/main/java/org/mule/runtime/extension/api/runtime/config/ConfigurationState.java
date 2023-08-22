/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionProvider;

import java.util.Map;

/**
 * Contains the state of the parameters of a given {@link ConfigurationInstance}.
 * <p>
 * The parameters will be segregated by config parameters (the ones defined in the config element itself) and the connection
 * parameters (the ones defined in the associated {@link ConnectionProvider}).
 *
 * @since 1.0
 */
@NoImplement
public interface ConfigurationState {

  /**
   * A {@link Map} which keys are the name of the config parameters and the values are the parameter values. It will not include
   * entries for parameters for which a value was not specified or was resolved to {@code null}
   *
   * @return an unmodifiable {@link Map}. Might be empty but will never be {@code null}
   */
  Map<String, Object> getConfigParameters();

  /**
   * A {@link Map} which keys are the name of the {@link ConnectionProvider} parameters and the values are the parameter values.
   * It the owning {@link ConfigurationInstance} does not have an associated {@link ConnectionProvider}, then an empty map will be
   * returned.
   * <p>
   * It will not include entries for parameters for which a value was not specified or was resolved to {@code null}.
   *
   * @return an unmodifiable {@link Map}. Might be empty but will never be {@code null}
   */
  Map<String, Object> getConnectionParameters();
}
