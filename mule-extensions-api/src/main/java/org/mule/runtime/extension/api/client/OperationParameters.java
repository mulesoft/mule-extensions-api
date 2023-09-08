/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;


import org.mule.api.annotation.NoImplement;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A simple interface for parameters that aims to be used to execute an extension operation using the {@link ExtensionsClient}.
 *
 * @since 1.0
 * @deprecated since 1.5.0. Use {@link ExtensionsClient#execute(String, String, Consumer)} instead
 */
@MinMuleVersion("4.1")
@NoImplement
@Deprecated
public interface OperationParameters {

  /**
   * @return an {@link Optional} with the name of the config used to execute the operation, {@link Optional#empty()} in the case
   *         that no config name was provided for config-less operations.
   */
  Optional<String> getConfigName();

  /**
   * @return a {@link Map} with all the parameters required to execute an extension operation. If no parameters are required then
   *         an empty {@link Map} should be returned.
   *         <p>
   *         The config name parameter should NOT be on this parameters.
   */
  Map<String, Object> get();
}
