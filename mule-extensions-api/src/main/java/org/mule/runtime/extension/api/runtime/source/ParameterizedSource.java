/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;

import java.util.Map;

/**
 * A contract for obtaining the parameters that were configured into a {@link Source} instance without depending on its actual
 * implementation class.
 *
 * @since 1.0
 */
@NoImplement
public interface ParameterizedSource {

  /**
   * Returns a {@link Map} with the parameters that the source uses for initialising itself. Notice that this will not include any
   * parameters used for the {@link OnSuccess} or {@link OnError} callbacks.
   *
   * @return the source initial parameters.
   */
  Map<String, Object> getInitialisationParameters();
}
